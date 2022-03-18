package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.entity.SysTenantEntity;
import cc.uncarbon.module.sys.entity.SysUserRoleRelationEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysTenantMapper;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysRoleDTO;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysUserDTO;
import cc.uncarbon.module.sys.model.request.AdminInsertSysTenantDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysTenantDTO;
import cc.uncarbon.module.sys.model.request.AdminUpdateSysTenantDTO;
import cc.uncarbon.module.sys.model.response.SysTenantBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 系统租户
 * @author Uncarbon
 */
@Slf4j
@Service
public class SysTenantService extends HelioBaseServiceImpl<SysTenantMapper, SysTenantEntity> {

    private final SysRoleService sysRoleService;

    private final SysUserRoleRelationService sysUserRoleRelationService;

    private final SysUserService sysUserService;

    public SysTenantService(SysRoleService sysRoleService,
                            SysUserRoleRelationService sysUserRoleRelationService,
                            // 缓解循环依赖，其实不建议这么做
                            @Lazy SysUserService sysUserService
    ) {
        this.sysRoleService = sysRoleService;
        this.sysUserRoleRelationService = sysUserRoleRelationService;
        this.sysUserService = sysUserService;
    }

    /**
     * 后台管理-分页列表
     */
    public PageResult<SysTenantBO> adminList(PageParam pageParam, AdminListSysTenantDTO dto) {
        Page<SysTenantEntity> entityPage = this.page(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysTenantEntity>()
                        .lambda()
                        // 租户名
                        .like(StrUtil.isNotBlank(dto.getTenantName()), SysTenantEntity::getTenantName, StrUtil.cleanBlank(dto.getTenantName()))
                        // 租户ID
                        .eq(ObjectUtil.isNotNull(dto.getTenantId()), SysTenantEntity::getTenantId, dto.getTenantId())
                        // 状态
                        .eq(ObjectUtil.isNotNull(dto.getStatus()), SysTenantEntity::getStatus, dto.getStatus())
                        // 时间区间
                        .between(ObjectUtil.isNotNull(dto.getBeginAt()) && ObjectUtil.isNotNull(dto.getEndAt()), SysTenantEntity::getCreatedAt, dto.getBeginAt(), dto.getEndAt())
                        // 排序
                        .orderByDesc(SysTenantEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
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
        SysTenantEntity entity = this.getById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    @SysLog(value = "新增系统租户")
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertSysTenantDTO dto) {
        log.info("[后台管理-新增系统租户] >> 入参={}", dto);
        this.checkExistence(dto);

        // 1. 加入一个新租户(tenant)
        //    这里是直接顺带创建管理员账号了, 你可以根据业务需要决定是否创建

        dto.setId(null);
        SysTenantEntity entity = new SysTenantEntity();
        BeanUtil.copyProperties(dto, entity);

        this.save(entity);

        Long newTenantEntityId = entity.getId();
        Long newTenantId = entity.getTenantId();

        // 2. 创建一个新角色(role)
        //    注意:这里并没有指派其可见菜单
        Long newRoleId = sysRoleService.adminInsert(
                AdminInsertOrUpdateSysRoleDTO.builder()
                        .tenantId(newTenantId)
                        .title(dto.getTenantName() + "管理员")
                        .value("Admin")
                        .build()
        );

        // 3. 创建一个新用户
        Long newUserId = sysUserService.adminInsert(
                AdminInsertOrUpdateSysUserDTO.builder()
                        .tenantId(newTenantId)
                        .username(dto.getTenantAdminUsername())
                        .passwordOfNewUser(dto.getTenantAdminPassword())
                        .nickname(dto.getTenantName() + "管理员")
                        .email(dto.getTenantAdminEmail())
                        .phoneNo(dto.getTenantAdminPhoneNo())
                        .build()
        );

        // 4. 将新用户绑定至新角色上
        sysUserRoleRelationService.save(
                SysUserRoleRelationEntity.builder()
                        .tenantId(newTenantId)
                        .userId(newUserId)
                        .roleId(newRoleId)
                        // 可能不会自动填充字段，手动补上
                        .createdAt(LocalDateTime.now())
                        .createdBy(UserContextHolder.getUserContext().getUserName())
                        .updatedAt(LocalDateTime.now())
                        .updatedBy(UserContextHolder.getUserContext().getUserName())
                        .build()
        );

        entity = new SysTenantEntity();
        entity
                .setTenantAdminUserId(newUserId)
                .setId(newTenantEntityId);

        // 5. 把管理员账号更新进库
        this.updateById(entity);

        return newTenantId;
    }

    /**
     * 后台管理-编辑
     */
    @SysLog(value = "编辑系统租户")
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminUpdateSysTenantDTO dto) {
        log.info("[后台管理-编辑系统租户] >> 入参={}", dto);
        this.checkExistence(dto);

        SysTenantEntity entity = new SysTenantEntity();
        BeanUtil.copyProperties(dto, entity);

        this.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @SysLog(value = "删除系统租户")
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除系统租户] >> 入参={}", ids);
        this.removeByIds(ids);
    }

    /**
     * 根据租户ID(非主键ID)，得到租户实体
     */
    public SysTenantEntity getTenantEntityByTenantId(Long tenantId) {
        return this.getOne(
                new QueryWrapper<SysTenantEntity>()
                        .lambda()
                        .eq(SysTenantEntity::getTenantId, tenantId)
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );
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
    private SysTenantBO entity2BO(SysTenantEntity entity) {
        if (entity == null) {
            return null;
        }

        SysTenantBO bo = new SysTenantBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        if (ObjectUtil.isNotNull(entity.getTenantAdminUserId())) {
            bo
                    .setTenantAdminUser(sysUserService.getBaseInfoById(entity.getTenantAdminUserId()))
            ;
        }

        return bo;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @return BO List
     */
    private List<SysTenantBO> entityList2BOs(List<SysTenantEntity> entityList) {
        // 深拷贝
        List<SysTenantBO> ret = new ArrayList<>(entityList.size());
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
    private PageResult<SysTenantBO> entityPage2BOPage(Page<SysTenantEntity> entityPage) {
        PageResult<SysTenantBO> ret = new PageResult<>();
        BeanUtil.copyProperties(entityPage, ret);
        ret.setRecords(this.entityList2BOs(entityPage.getRecords()));

        return ret;
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminUpdateSysTenantDTO dto) {
        SysTenantEntity existingEntity = this.getOne(
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

}
