package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.function.StreamFunction;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.module.sys.entity.SysRoleEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysRoleMapper;
import cc.uncarbon.module.sys.model.request.AdminBindRoleMenuRelationDTO;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysRoleDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysRoleDTO;
import cc.uncarbon.module.sys.model.response.SysRoleBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 后台角色
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysRoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleRelationService sysUserRoleRelationService;
    private final SysRoleMenuRelationService sysRoleMenuRelationService;
    private final SysMenuService sysMenuService;


    /**
     * 后台管理-分页列表
     */
    public PageResult<SysRoleBO> adminList(PageParam pageParam, AdminListSysRoleDTO dto) {
        Page<SysRoleEntity> entityPage = sysRoleMapper.selectPage(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysRoleEntity>()
                        .lambda()
                        // 名称
                        .like(StrUtil.isNotBlank(dto.getTitle()), SysRoleEntity::getTitle, StrUtil.cleanBlank(dto.getTitle()))
                        // 值
                        .like(StrUtil.isNotBlank(dto.getValue()), SysRoleEntity::getValue, StrUtil.cleanBlank(dto.getValue()))
                        // 排序
                        .orderByDesc(SysRoleEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public SysRoleBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public SysRoleBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        SysRoleEntity entity = sysRoleMapper.selectById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     *
     * @return 主键ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysRoleDTO dto) {
        log.info("[后台管理-新增后台角色] >> 入参={}", dto);
        this.checkExistence(dto);

        dto.setId(null);
        SysRoleEntity entity = new SysRoleEntity();
        BeanUtil.copyProperties(dto, entity);

        sysRoleMapper.insert(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateSysRoleDTO dto) {
        log.info("[后台管理-编辑后台角色] >> 入参={}", dto);
        this.checkExistence(dto);

        SysRoleEntity entity = new SysRoleEntity();
        BeanUtil.copyProperties(dto, entity);

        sysRoleMapper.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除后台角色] >> 入参={}", ids);
        sysRoleMapper.deleteBatchIds(ids);
    }

    /**
     * 后台管理-绑定角色与菜单关联关系
     *
     * @return 新菜单ID集合对应的权限名
     */
    @Transactional(rollbackFor = Exception.class)
    public Set<String> adminBindMenus(AdminBindRoleMenuRelationDTO dto) {
        Set<String> newPermissions = sysMenuService.listPermissionsByMenuIds(dto.getMenuIds());
        sysRoleMenuRelationService.cleanAndBind(dto.getRoleId(), dto.getMenuIds());

        return newPermissions;
    }


    /*
    ----------------------------------------------------------------
                        私有方法 private methods
    ----------------------------------------------------------------
     */

    /**
     * 实体转 BO
     *
     * @param entity 实体
     * @return BO
     */
    private SysRoleBO entity2BO(SysRoleEntity entity) {
        if (entity == null) {
            return null;
        }

        SysRoleBO bo = new SysRoleBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        bo.setMenuIds(sysRoleMenuRelationService.listMenuIdsByRoleIds(Collections.singleton(bo.getId())));
        return bo;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @return BO List
     */
    private List<SysRoleBO> entityList2BOs(List<SysRoleEntity> entityList) {
        // 深拷贝
        List<SysRoleBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity))
        );

        return ret;
    }

    /**
     * 实体分页转 BO 分页
     *
     * @param entityPage 实体分页
     * @return BO 分页
     */
    private PageResult<SysRoleBO> entityPage2BOPage(Page<SysRoleEntity> entityPage) {
        return new PageResult<SysRoleBO>()
                .setCurrent(entityPage.getCurrent())
                .setSize(entityPage.getSize())
                .setTotal(entityPage.getTotal())
                .setRecords(this.entityList2BOs(entityPage.getRecords()))
                ;
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminInsertOrUpdateSysRoleDTO dto) {
        SysRoleEntity existingEntity = sysRoleMapper.selectOne(
                new QueryWrapper<SysRoleEntity>()
                        .lambda()
                        // 仅取主键ID
                        .select(SysRoleEntity::getId)
                        // 名称相同
                        .eq(SysRoleEntity::getTitle, dto.getTitle())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同后台角色，请重新输入");
        }
    }

    /**
     * 取用户ID拥有角色对应的 角色ID-角色名 map
     *
     * @param userId 用户ID
     * @return 失败返回空 map
     */
    public Map<Long, String> getRoleMapByUserId(Long userId) {
        Set<Long> roleIds = sysUserRoleRelationService.listRoleIdsByUserId(userId);

        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyMap();
        }

        // 根据角色Ids取 map
        return sysRoleMapper.selectList(
                new QueryWrapper<SysRoleEntity>()
                        .lambda()
                        .select(SysRoleEntity::getId, SysRoleEntity::getValue)
                        .in(SysRoleEntity::getId, roleIds)
        ).stream().collect(Collectors.toMap(SysRoleEntity::getId, SysRoleEntity::getValue, StreamFunction.ignoredThrowingMerger()));
    }
}
