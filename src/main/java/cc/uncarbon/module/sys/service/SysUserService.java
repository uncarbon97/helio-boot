package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.TenantContext;
import cc.uncarbon.framework.core.context.TenantContextHolder;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.entity.SysUserEntity;
import cc.uncarbon.module.sys.enums.GenericStatusEnum;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.enums.SysUserStatusEnum;
import cc.uncarbon.module.sys.mapper.SysUserMapper;
import cc.uncarbon.module.sys.model.request.*;
import cc.uncarbon.module.sys.model.response.*;
import cc.uncarbon.module.sys.util.PwdUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 后台用户
 *
 * @author Uncarbon
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysUserService extends HelioBaseServiceImpl<SysUserMapper, SysUserEntity> {

    private final SysRoleService sysRoleService;

    private final SysDeptService sysDeptService;

    private final SysMenuService sysMenuService;

    private final SysTenantService sysTenantService;

    private final SysUserDeptRelationService sysUserDeptRelationService;

    private final SysUserRoleRelationService sysUserRoleRelationService;


    /**
     * 后台管理-分页列表
     */
    public PageResult<SysUserBO> adminList(PageParam pageParam, AdminListSysUserDTO dto) {
        Page<SysUserEntity> entityPage = this.page(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysUserEntity>()
                        .lambda()
                        // 手机号
                        .like(StrUtil.isNotBlank(dto.getPhoneNo()), SysUserEntity::getPhoneNo, StrUtil.cleanBlank(dto.getPhoneNo()))
                        // 排序
                        .orderByDesc(SysUserEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 通用-详情
     *
     * @deprecated 使用 getOneById(java.lang.Long, boolean, boolean) 替代
     */
    @Deprecated
    public SysUserBO getOneById(Long entityId) throws BusinessException {
        return this.getOneById(entityId, true, false);
    }

    /**
     * 通用-详情
     *
     * @param entityId                    实体类主键ID
     * @param throwIfInvalidId            是否在 ID 无效时抛出异常
     * @param joinFullRolesAndPermissions 是否显示完整角色和权限信息
     * @return null or BO
     */
    public SysUserBO getOneById(Long entityId, boolean throwIfInvalidId, boolean joinFullRolesAndPermissions) throws BusinessException {
        SysUserEntity entity = this.getById(entityId);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity, joinFullRolesAndPermissions);
    }

    /**
     * 后台管理-新增
     *
     * @return 主键ID
     */
    @SysLog(value = "新增后台用户")
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysUserDTO dto) {
        log.info("[后台管理-新增后台用户] >> DTO={}", dto);
        this.checkExistence(dto);

        dto.setId(null);
        SysUserEntity entity = new SysUserEntity();
        BeanUtil.copyProperties(dto, entity);

        String salt = IdUtil.randomUUID();
        entity
                .setSalt(salt)
                .setPin(dto.getUsername())
                .setPwd(PwdUtil.encrypt(dto.getPasswordOfNewUser(), salt))
        ;

        this.save(entity);

        sysUserDeptRelationService.cleanAndBind(entity.getId(), dto.getDeptId());

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @SysLog(value = "编辑后台用户")
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateSysUserDTO dto) {
        log.info("[后台管理-编辑后台用户] >> DTO={}", dto);
        this.checkExistence(dto);

        SysUserEntity updateEntity = new SysUserEntity();
        BeanUtil.copyProperties(dto, updateEntity);

        sysUserDeptRelationService.cleanAndBind(dto.getId(), dto.getDeptId());

        this.updateById(updateEntity);
    }

    /**
     * 后台管理-删除
     */
    @SysLog(value = "删除后台用户")
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除后台用户] >> ids={}", ids);
        this.removeByIds(ids);
    }

    /**
     * 后台管理-登录
     */
    @SysLog(value = "登录后台用户")
    public SysUserLoginBO adminLogin(SysUserLoginDTO dto) {
        // 主动清空用户上下文，避免尴尬
        UserContextHolder.setUserContext(null);

        /*
        这里是实际启用了多租户功能，并允许前端主动传欲登录的租户ID
        实际生产应用时，可以根据业务需要加密等，然后在这里解密
         */
        if (TenantContextHolder.isTenantEnabled()
            && ObjectUtil.isNotNull(dto.getTenantId())) {

            // 查询所属租户是否仍有效，这里是直接查库，并发高的情况下可以改造为从缓存读取
            SysTenantBO tenantInfo = sysTenantService.getTenantByTenantId(dto.getTenantId());
            if (tenantInfo == null) {
                throw new BusinessException(SysErrorEnum.INVALID_TENANT);
            }

            if (GenericStatusEnum.DISABLED.equals(tenantInfo.getStatus())) {
                throw new BusinessException(SysErrorEnum.DISABLED_TENANT);
            }

            // 验证通过，将所属租户写入租户上下文，使得多租户可以正确执行
            TenantContextHolder.setTenantContext(
                    TenantContext.builder()
                            .tenantId(tenantInfo.getTenantId())
                            .tenantName(tenantInfo.getTenantName())
                            .build()
            );
        }

        SysUserEntity sysUserEntity = this.getUserByPin(dto.getUsername());
        if (sysUserEntity == null) {
            throw new BusinessException(SysErrorEnum.USER_NOT_EXISTS);
        }

        if (!PwdUtil.encrypt(dto.getPassword(), sysUserEntity.getSalt()).equals(sysUserEntity.getPwd())) {
            throw new BusinessException(SysErrorEnum.INCORRECT_USER_PASSWORD);
        }

        if (SysUserStatusEnum.BANNED.equals(sysUserEntity.getStatus())) {
            throw new BusinessException(SysErrorEnum.BANNED_USER);
        }

        /*
        以上为有效性校验, 进入实际业务逻辑
        ---------------------------------------------------
         */

        try {
            this.getBaseMapper().updateLastLoginAt(sysUserEntity.getId(), LocalDateTimeUtil.now());
        } catch (Exception ignored) {
            // 实际开发环境请删除本try-catch块
        }


        // 取账号完整BO
        SysUserBO sysUserBO = this.getOneById(sysUserEntity.getId(), true, true);

        SysUserLoginBO ret = new SysUserLoginBO();
        BeanUtil.copyProperties(sysUserBO, ret);

        // 因字段类型不一致, 单独转换
        ret
                .setRoleIds(sysUserBO.getRoleMap().keySet())
                .setRoles(sysUserBO.getRoleMap().values())
                .setPermissions(sysUserBO.getPermissions())
        ;

        return ret;
    }

    /**
     * 后台管理-取当前用户信息
     */
    public VbenAdminUserInfoBO adminGetCurrentUserInfo() {
        SysUserBO sysUserBO = this.getOneById(UserContextHolder.getUserId(), true, false);
        return VbenAdminUserInfoBO.builder()
                .username(sysUserBO.getUsername())
                .nickname(sysUserBO.getNickname())
                .lastLoginAt(sysUserBO.getLastLoginAt())
                .build();
    }

    /**
     * 后台管理-重置某用户密码
     */
    @SysLog(value = "重置某用户密码")
    public void adminResetUserPassword(AdminResetSysUserPasswordDTO dto) {
        SysUserEntity sysUserEntity = this.getById(dto.getUserId());

        SysUserEntity templateEntity = new SysUserEntity();
        templateEntity
                .setPwd(PwdUtil.encrypt(dto.getRandomPassword(), sysUserEntity.getSalt()))
                .setId(dto.getUserId())
        ;

        this.updateById(templateEntity);
    }

    /**
     * 后台管理-修改当前用户密码
     */
    @SysLog(value = "修改当前用户密码")
    public void adminUpdateCurrentUserPassword(AdminUpdateCurrentSysUserPasswordDTO dto) {
        SysUserEntity sysUserEntity = this.getById(UserContextHolder.getUserId());
        if (sysUserEntity == null || !sysUserEntity.getPwd().equals(PwdUtil.encrypt(dto.getOldPassword(), sysUserEntity.getSalt()))) {
            throw new BusinessException(SysErrorEnum.INCORRECT_OLD_PASSWORD);
        }

        sysUserEntity
                .setPwd(PwdUtil.encrypt(dto.getConfirmNewPassword(), sysUserEntity.getSalt()))
                .setId(UserContextHolder.getUserId())
        ;

        this.updateById(sysUserEntity);
    }

    /**
     * 通用-根据用户账号查询
     */
    public SysUserEntity getUserByPin(String pin) {
        return this.getOne(
                new QueryWrapper<SysUserEntity>()
                        .lambda()
                        .eq(SysUserEntity::getPin, pin)
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );
    }

    /**
     * 通用-取用户基本信息
     */
    public SysUserBaseInfoBO getBaseInfoById(Long entityId) {
        return this.getBaseMapper().getBaseInfoByUserId(entityId);
    }

    /**
     * 后台管理-绑定用户与角色关联关系
     */
    public void adminBindRoles(AdminBindUserRoleRelationDTO dto) {
        sysUserRoleRelationService.cleanAndBind(dto.getUserId(), dto.getRoleIds());
    }

    /*
    私  有  方  法
    ------------------------------------------------------------------------------------------------
     */

    /**
     * 实体转 BO
     * @param entity 实体对象
     * @param joinPermissions 追加权限字符串
     * @return
     */
    private SysUserBO entity2BO(SysUserEntity entity, boolean joinPermissions) {
        if (entity == null) {
            return null;
        }

        SysUserBO bo = new SysUserBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        Map<Long, String> roleMap = sysRoleService.getRoleMapByUserId(bo.getId());
        bo
                .setRoleMap(roleMap)
                .setRoleIds(roleMap.keySet());

        SysDeptBO dept = sysDeptService.getPlainDeptByUserId(bo.getId());
        if (dept != null) {
            bo
                    .setDeptId(dept.getId())
                    .setDeptTitle(dept.getTitle())
            ;
        }

        if (joinPermissions) {
            bo.setPermissions(sysMenuService.listPermissionByRoleIds(roleMap.keySet()));
        }

        return bo;
    }

    private PageResult<SysUserBO> entityPage2BOPage(Page<SysUserEntity> entityPage) {
        // 深拷贝
        List<SysUserBO> boRecords = new ArrayList<>(entityPage.getRecords().size());
        entityPage.getRecords().forEach(
                entity -> boRecords.add(this.entity2BO(entity, false))
        );

        PageResult<SysUserBO> ret = new PageResult<>();
        BeanUtil.copyProperties(entityPage, ret);
        ret.setRecords(boRecords);
        return ret;
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminInsertOrUpdateSysUserDTO dto) {
        SysUserEntity existingEntity = this.getUserByPin(dto.getUsername());

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同账号，请重新输入");
        }
    }
}
