package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.TenantContext;
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
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 后台用户
 * @author Uncarbon
 */
@Slf4j
@Service
public class SysUserService extends HelioBaseServiceImpl<SysUserMapper, SysUserEntity> {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysTenantService sysTenantService;

    @Resource
    private SysUserDeptRelationService sysUserDeptRelationService;

    @Resource
    private SysUserRoleRelationService sysUserRoleRelationService;


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
     */
    public SysUserBO getOneById(Long entityId) {
        SysUserEntity entity = this.getById(entityId);
        SysErrorEnum.INVALID_ID.assertNotNull(entity);

        return this.entity2BO(entity, true);
    }

    /**
     * 后台管理-新增
     * @return 主键ID
     */
    @SysLog(value = "新增后台用户")
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysUserDTO dto) {
        this.checkExist(dto);

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
        this.checkExist(dto);

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
    public void adminDelete(List<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 后台管理-登录
     */
    @SysLog(value = "登录后台用户")
    public SysUserLoginBO adminLogin(SysUserLoginDTO dto) {
        // 主动清空用户上下文，避免残留租户ID导致的尴尬
        UserContextHolder.setUserContext(null);

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

        // 查询所属租户是否有效
        SysTenantBO tenantInfo = sysTenantService.getTenantByTenantId(sysUserEntity.getTenantId());
        if (tenantInfo == null) {
            throw new BusinessException(SysErrorEnum.INVALID_TENANT);
        }

        if (GenericStatusEnum.DISABLED.equals(tenantInfo.getStatus())) {
            throw new BusinessException(SysErrorEnum.DISABLED_TENANT);
        }

        /*
        以上为有效性校验, 进入实际业务逻辑
        ---------------------------------------------------
         */

        // 将所属租户写入到用户上下文，使得 Mybatis-Plus 多租户拦截器可以正确执行到对应租户ID
        TenantContext currentTenantContext = TenantContext.builder()
                .tenantId(tenantInfo.getTenantId())
                .tenantName(tenantInfo.getTenantName())
                .build();
        UserContextHolder.setRelationalTenant(currentTenantContext);

        try {
            this.getBaseMapper().updateLastLoginAt(sysUserEntity.getId(), LocalDateTimeUtil.now());
        } catch (Exception ignored) {
            // 实际开发环境请删除本try-catch块
        }


        // 取账号完整BO
        SysUserBO sysUserBO = this.getOneById(sysUserEntity.getId());

        SysUserLoginBO ret = new SysUserLoginBO();
        BeanUtil.copyProperties(sysUserBO, ret);

        // 因字段类型不一致, 单独转换
        ret
                .setRoles(sysUserBO.getRoles().stream().map(SysRoleBO::getValue).collect(Collectors.toList()))
                .setPermissions(sysUserBO.getPermissions())
                .setRelationalTenant(currentTenantContext)
        ;

        return ret;
    }

    /**
     * 后台管理-取当前用户信息
     */
    public VbenAdminUserInfoBO adminGetCurrentUserInfo() {
        SysUserBO sysUserBO = getOneById(UserContextHolder.getUserId());
        return VbenAdminUserInfoBO.builder()
                .username(sysUserBO.getUsername())
                .nickname(sysUserBO.getNickname())
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

    private SysUserBO entity2BO(SysUserEntity entity, boolean joinFullRolesAndPermissions) {
        if (entity == null) {
            return null;
        }

        SysUserBO bo = new SysUserBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        bo.setRoleIds(sysUserRoleRelationService.listRoleIdByUserId(bo.getId()));

        SysDeptBO dept = sysDeptService.getPlainDeptByUserId(bo.getId());
        if (dept != null) {
            bo
                    .setDeptId(dept.getId())
                    .setDeptTitle(dept.getTitle())
            ;
        }

        if (joinFullRolesAndPermissions) {
            List<SysRoleBO> roleBOs = sysRoleService.listRoleByUserId(bo.getId());
            bo
                    .setRoles(roleBOs)
                    .setPermissions(sysMenuService.adminListPermissionByRoleIds(roleBOs.stream().map(SysRoleBO::getId).collect(Collectors.toList())))
            ;
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
     * 检查是否已存在同名数据
     * @param dto DTO
     */
    private void checkExist(AdminInsertOrUpdateSysUserDTO dto) {
        SysUserEntity existEntity = this.getUserByPin(dto.getUsername());

        if (existEntity != null && !existEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同账号，请重新输入");
        }
    }
}