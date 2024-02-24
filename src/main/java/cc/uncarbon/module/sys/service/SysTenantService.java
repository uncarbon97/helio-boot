package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.module.sys.entity.SysTenantEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysTenantMapper;
import cc.uncarbon.module.sys.mapper.SysUserMapper;
import cc.uncarbon.module.sys.model.request.AdminInsertSysTenantDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysTenantDTO;
import cc.uncarbon.module.sys.model.request.AdminUpdateSysTenantDTO;
import cc.uncarbon.module.sys.model.response.SysTenantBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * 系统租户
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysTenantService {

    private final SysTenantMapper sysTenantMapper;
    // 比较尴尬，为避免循环依赖，只能这里引用SysUserMapper拿用户信息
    private final SysUserMapper sysUserMapper;


    /**
     * 后台管理-分页列表
     */
    public PageResult<SysTenantBO> adminList(PageParam pageParam, AdminListSysTenantDTO dto) {
        Page<SysTenantEntity> entityPage = sysTenantMapper.selectPage(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysTenantEntity>()
                        .lambda()
                        // 租户名
                        .like(CharSequenceUtil.isNotBlank(dto.getTenantName()), SysTenantEntity::getTenantName, CharSequenceUtil.cleanBlank(dto.getTenantName()))
                        // 租户ID
                        .eq(ObjectUtil.isNotNull(dto.getTenantId()), SysTenantEntity::getTenantId, dto.getTenantId())
                        // 状态
                        .eq(ObjectUtil.isNotNull(dto.getStatus()), SysTenantEntity::getStatus, dto.getStatus())
                        // 排序
                        .orderByDesc(SysTenantEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage, true);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public SysTenantBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public SysTenantBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        SysTenantEntity entity = sysTenantMapper.selectById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity, true);
    }

    /**
     * 后台管理-新增
     * 注：本方法较为特殊，仅供SysTenantFacadeImpl调用
     */
    @Transactional(rollbackFor = Exception.class)
    public SysTenantEntity adminInsert(AdminInsertSysTenantDTO dto) {
        dto.setId(null);
        SysTenantEntity entity = new SysTenantEntity();
        BeanUtil.copyProperties(dto, entity);

        sysTenantMapper.insert(entity);

        return entity;
    }

    /**
     * 后台管理-编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminUpdateSysTenantDTO dto) {
        log.info("[后台管理-编辑系统租户] >> 入参={}", dto);

        SysTenantEntity entity = new SysTenantEntity();
        BeanUtil.copyProperties(dto, entity);

        sysTenantMapper.updateById(entity);
    }

    /**
     * 后台管理-编辑
     * 注：本方法较为特殊，仅供SysTenantFacadeImpl调用
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(SysTenantEntity entity) {
        sysTenantMapper.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除系统租户] >> 入参={}", ids);
        sysTenantMapper.deleteBatchIds(ids);
    }

    /**
     * 根据租户ID(非主键ID)，得到租户实体
     */
    public SysTenantEntity getTenantEntityByTenantId(Long tenantId) {
        return sysTenantMapper.selectOne(
                new QueryWrapper<SysTenantEntity>()
                        .lambda()
                        .eq(SysTenantEntity::getTenantId, tenantId)
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    public void checkExistence(AdminInsertSysTenantDTO dto) {
        SysTenantEntity existingEntity = sysTenantMapper.selectOne(
                new QueryWrapper<SysTenantEntity>()
                        .lambda()
                        // 仅取主键ID
                        .select(SysTenantEntity::getId)
                        // 租户ID相同
                        .eq(SysTenantEntity::getTenantId, dto.getTenantId())
                        .or()
                        // 或租户名相同
                        .eq(SysTenantEntity::getTenantName, dto.getTenantName())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同系统租户，请重新输入");
        }
    }

    /**
     * 根据主键IDs，取租户BOs
     * @param fillTenantAdminUser 是否根据租户管理员用户ID，查询关联用户信息并填充到BO
     */
    public List<SysTenantBO> listByIds(Collection<Long> ids, boolean fillTenantAdminUser) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<SysTenantEntity> entityList = sysTenantMapper.selectBatchIds(ids);
        return entityList2BOs(entityList, fillTenantAdminUser);
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
     * @param fillTenantAdminUser 是否根据租户管理员用户ID，查询关联用户信息并填充到BO
     * @return BO
     */
    private SysTenantBO entity2BO(SysTenantEntity entity, boolean fillTenantAdminUser) {
        if (entity == null) {
            return null;
        }

        SysTenantBO bo = new SysTenantBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        if (fillTenantAdminUser && ObjectUtil.isNotNull(entity.getTenantAdminUserId())) {
            bo.setTenantAdminUser(sysUserMapper.getBaseInfoByUserId(entity.getTenantAdminUserId()));
        }

        return bo;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @param fillTenantAdminUser 是否根据租户管理员用户ID，查询关联用户信息并填充到BO
     * @return BO List
     */
    private List<SysTenantBO> entityList2BOs(List<SysTenantEntity> entityList, boolean fillTenantAdminUser) {
        // 深拷贝
        List<SysTenantBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity, fillTenantAdminUser))
        );

        return ret;
    }

    /**
     * 实体分页转 BO 分页
     *
     * @param entityPage 实体分页
     * @return BO 分页
     */
    private PageResult<SysTenantBO> entityPage2BOPage(Page<SysTenantEntity> entityPage, boolean fillTenantAdminUser) {
        return new PageResult<SysTenantBO>()
                .setCurrent(entityPage.getCurrent())
                .setSize(entityPage.getSize())
                .setTotal(entityPage.getTotal())
                .setRecords(this.entityList2BOs(entityPage.getRecords(), fillTenantAdminUser));
    }

}
