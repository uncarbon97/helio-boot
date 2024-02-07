package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.function.StreamFunction;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.entity.SysRoleEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysRoleMapper;
import cc.uncarbon.module.sys.model.interior.UserRoleContainer;
import cc.uncarbon.module.sys.model.request.AdminBindRoleMenuRelationDTO;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysRoleDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysRoleDTO;
import cc.uncarbon.module.sys.model.response.SysRoleBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
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
        Set<Long> invisibleRoleIds = determineInvisibleRoleIds();
        Page<SysRoleEntity> entityPage = sysRoleMapper.selectPage(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysRoleEntity>()
                        .lambda()
                        // 名称
                        .like(CharSequenceUtil.isNotBlank(dto.getTitle()), SysRoleEntity::getTitle, CharSequenceUtil.cleanBlank(dto.getTitle()))
                        // 值
                        .like(CharSequenceUtil.isNotBlank(dto.getValue()), SysRoleEntity::getValue, CharSequenceUtil.cleanBlank(dto.getValue()))
                        // 不显示特定角色
                        .notIn(CollUtil.isNotEmpty(invisibleRoleIds), SysRoleEntity::getId, invisibleRoleIds)
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

        return this.entity2BO(entity, true);
    }

    /**
     * 后台管理-新增
     *
     * @return 主键ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysRoleDTO dto) {
        log.info("[后台管理-新增后台角色] >> 入参={}", dto);
        preInsertOrUpdateCheck(dto);
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
        preInsertOrUpdateCheck(dto);
        this.checkExistence(dto);

        // 暂不检查该角色是否为当前用户关联的角色

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
        preDeleteCheck(ids);
        sysRoleMapper.deleteBatchIds(ids);
    }

    /**
     * 后台管理-绑定角色与菜单关联关系
     *
     * @return 新菜单ID集合对应的权限名
     */
    @Transactional(rollbackFor = Exception.class)
    public Set<String> adminBindMenus(AdminBindRoleMenuRelationDTO dto) {
        preBindRoleMenuRelationCheck(dto);
        Set<String> newPermissions = sysMenuService.listPermissionsByMenuIds(dto.getMenuIds());
        sysRoleMenuRelationService.cleanAndBind(dto.getRoleId(), dto.getMenuIds());

        return newPermissions;
    }

    /**
     * 后台管理-下拉框数据
     */
    public List<SysRoleBO> adminSelectOptions() {
        Set<Long> invisibleRoleIds = determineInvisibleRoleIds();
        List<SysRoleEntity> entityList = sysRoleMapper.selectList(
                new QueryWrapper<SysRoleEntity>()
                        .lambda()
                        // 只取特定字段
                        .select(SysRoleEntity::getId, SysRoleEntity::getTitle)
                        // 不显示特定角色
                        .notIn(CollUtil.isNotEmpty(invisibleRoleIds), SysRoleEntity::getId, invisibleRoleIds)
                        // 排序
                        .orderByAsc(SysRoleEntity::getId)
        );
        // 无需填充菜单IDs
        return entityList2BOs(entityList, false);
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

    /**
     * 取当前用户关联角色容器
     * 仅内部使用
     */
    protected UserRoleContainer getCurrentUserRoleContainer() {
        return getSpecifiedUserRoleContainer(UserContextHolder.getUserId());
    }

    /**
     * 取指定用户关联角色容器
     * 仅内部使用
     */
    protected UserRoleContainer getSpecifiedUserRoleContainer(Long specifiedUserId) {
        Set<Long> currentUserRoleIds = sysUserRoleRelationService.listRoleIdsByUserId(specifiedUserId);
        List<SysRoleEntity> currentUserRoles = Collections.emptyList();
        if (CollUtil.isNotEmpty(currentUserRoleIds)) {
            currentUserRoles = sysRoleMapper.selectBatchIds(currentUserRoleIds);
        }
        return new UserRoleContainer(currentUserRoleIds, currentUserRoles);
    }

    /**
     * 确定不可见角色IDs
     * 仅内部使用
     * 租户管理员：列表中不显示超级管理员角色
     * 普通角色：列表中不显示超级管理员、租户管理员角色
     * @return mutable Set，支持外部改变元素
     */
    protected Set<Long> determineInvisibleRoleIds() {
        UserRoleContainer currentUser = getCurrentUserRoleContainer();
        // 超级管理员：不限制
        if (currentUser.isSuperAdmin()) {
            return new HashSet<>();
        }
        // 租户管理员：列表中不显示超级管理员角色
        if (currentUser.isTenantAdmin()) {
            return CollUtil.newHashSet(SysConstant.SUPER_ADMIN_ROLE_ID);
        }
        // 普通角色：列表中不显示超级管理员、租户管理员角色
        Set<Long> ret = sysRoleMapper.selectList(
                new QueryWrapper<SysRoleEntity>()
                        .lambda()
                        // 仅取主键ID
                        .select(SysRoleEntity::getId)
                        // 值相同
                        .eq(SysRoleEntity::getValue, SysConstant.TENANT_ADMIN_ROLE_VALUE)
        ).stream().map(SysRoleEntity::getId).collect(Collectors.toSet());
        ret.add(SysConstant.SUPER_ADMIN_ROLE_ID);
        return ret;
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
     * @param fillMenuIds 是否根据实体ID，查询关联菜单IDs并填充到BO
     * @return BO
     */
    private SysRoleBO entity2BO(SysRoleEntity entity, boolean fillMenuIds) {
        if (entity == null) {
            return null;
        }

        SysRoleBO bo = new SysRoleBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        if (fillMenuIds) {
            bo.setMenuIds(sysRoleMenuRelationService.listMenuIdsByRoleIds(Collections.singleton(bo.getId())));
        }
        return bo;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @param fillMenuIds 是否根据实体ID，查询关联菜单IDs并填充到BO
     * @return BO List
     */
    private List<SysRoleBO> entityList2BOs(List<SysRoleEntity> entityList, boolean fillMenuIds) {
        // 深拷贝
        List<SysRoleBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity, fillMenuIds))
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
                // 需填充菜单IDs
                .setRecords(this.entityList2BOs(entityPage.getRecords(), true));
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

        if (dto.creatingNewTenantAdmin()) {
            long qty = sysRoleMapper.selectCount(
                    new QueryWrapper<SysRoleEntity>()
                            .lambda()
                            // 租户ID相同
                            .eq(SysRoleEntity::getTenantId, dto.getTenantId())
                            // 值相同
                            .eq(SysRoleEntity::getValue, dto.getValue())
                            .last(HelioConstant.CRUD.SQL_LIMIT_1)
            );
            SysErrorEnum.NEED_DELETE_EXISTING_TENANT_ADMIN_ROLE.assertTrue(qty <= 0L, dto.getTenantId());
        }
    }

    /**
     * 新增/编辑后台角色信息前检查
     */
    private void preInsertOrUpdateCheck(AdminInsertOrUpdateSysRoleDTO dto) {
        if (SysConstant.SUPER_ADMIN_ROLE_VALUE.equalsIgnoreCase(dto.getValue())) {
            // 角色值不能为SuperAdmin
            throw new BusinessException(SysErrorEnum.ROLE_VALUE_CANNOT_BE, SysConstant.SUPER_ADMIN_ROLE_VALUE);
        }
        if (dto.creatingNewTenantAdmin()) {
            // 除非是新增租户时关联新增租户管理员角色，否则角色值不能为Admin
            throw new BusinessException(SysErrorEnum.ROLE_VALUE_CANNOT_BE, SysConstant.TENANT_ADMIN_ROLE_VALUE);
        }

        boolean isUpdating = Objects.nonNull(dto.getId());
        if (isUpdating) {
            SysRoleEntity existingRole = sysRoleMapper.selectById(dto.getId());
            SysErrorEnum.INVALID_ID.assertNotNull(existingRole);
            if (existingRole.isSuperAdmin() || existingRole.isTenantAdmin()) {
                // 原来角色值为SuperAdmin或Admin的，不能被改变
                throw new BusinessException(SysErrorEnum.ROLE_VALUE_CANNOT_BE, existingRole.getValue());
            }
        }
    }

    /**
     * 删除后台角色前检查
     */
    private void preDeleteCheck(Collection<Long> ids) {
        if (CollUtil.contains(ids, SysConstant.SUPER_ADMIN_ROLE_ID)) {
            throw new BusinessException(SysErrorEnum.CANNOT_DELETE_SUPER_ADMIN_ROLE);
        }

        List<SysRoleEntity> existingEntityList = sysRoleMapper.selectBatchIds(ids);
        for (SysRoleEntity item : existingEntityList) {
            if (item.isSuperAdmin()) {
                throw new BusinessException(SysErrorEnum.CANNOT_DELETE_SUPER_ADMIN_ROLE);
            }

            if (item.isTenantAdmin()) {
                throw new BusinessException(SysErrorEnum.CANNOT_DELETE_TENANT_ADMIN_ROLE);
            }
        }

        UserRoleContainer currentUser = getCurrentUserRoleContainer();
        if (CollUtil.containsAny(currentUser.getCurrentUserRoleIds(), ids)) {
            throw new BusinessException(SysErrorEnum.CANNOT_DELETE_SELF_ROLE);
        }
    }

    /**
     * 绑定后台角色与菜单关联关系前检查
     * 防止越权访问漏洞
     */
    private void preBindRoleMenuRelationCheck(AdminBindRoleMenuRelationDTO dto) {
        UserRoleContainer currentUser = getCurrentUserRoleContainer();
        if (SysConstant.SUPER_ADMIN_ROLE_ID.equals(dto.getRoleId())) {
            throw new BusinessException(SysErrorEnum.CANNOT_BIND_MENUS_FOR_SUPER_ADMIN_ROLE);
        }

        if (CollUtil.contains(currentUser.getCurrentUserRoleIds(), dto.getRoleId())) {
            // 不能动自身角色
            throw new BusinessException(SysErrorEnum.CANNOT_BIND_MENUS_FOR_SELF);
        }

        // 有且只有当前用户为超级管理员，才可以为租户管理员赋权
        SysRoleEntity targetRole = sysRoleMapper.selectById(dto.getRoleId());
        if (targetRole.isTenantAdmin() && !currentUser.isSuperAdmin()) {
            throw new BusinessException(SysErrorEnum.BEYOND_AUTHORITY);
        }

        if (CollUtil.isNotEmpty(dto.getMenuIds()) && !currentUser.isSuperAdmin()) {
            // 超级管理员之外的角色，都需要校验自身菜单范围是否满足输入值
            Set<Long> visibleMenuIds = sysRoleMenuRelationService.listMenuIdsByRoleIds(currentUser.getCurrentUserRoleIds());
            if (!CollUtil.containsAll(visibleMenuIds, dto.getMenuIds())) {
                // 可能存在超自身权限赋权
                throw new BusinessException(SysErrorEnum.BEYOND_AUTHORITY);
            }
        }
    }
}
