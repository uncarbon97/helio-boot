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
import cc.uncarbon.module.sys.model.request.AdminBindUserRoleRelationDTO;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysUserDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysUserDTO;
import cc.uncarbon.module.sys.model.request.AdminResetSysUserPasswordDTO;
import cc.uncarbon.module.sys.model.request.AdminUpdateCurrentSysUserPasswordDTO;
import cc.uncarbon.module.sys.model.request.SysUserLoginDTO;
import cc.uncarbon.module.sys.model.response.SysDeptBO;
import cc.uncarbon.module.sys.model.response.SysTenantBO;
import cc.uncarbon.module.sys.model.response.SysUserBO;
import cc.uncarbon.module.sys.model.response.SysUserBaseInfoBO;
import cc.uncarbon.module.sys.model.response.SysUserLoginBO;
import cc.uncarbon.module.sys.model.response.VbenAdminUserInfoBO;
import cc.uncarbon.module.sys.util.PwdUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        /*
        这里是实际启用了多租户功能，并信任前端主动传欲登录的租户ID
        实际生产应用时，推荐前端传值加密，后端在此解密
         */
        TenantContext tenantContext = null;
        if (TenantContextHolder.isTenantEnabled() && ObjectUtil.isNotNull(dto.getTenantId())) {
            // 验证通过，将所属租户写入租户上下文，使得 SQL 拦截器可以正确执行
            tenantContext = this.checkAndGetTenantContext(dto.getTenantId());
            TenantContextHolder.setTenantContext(tenantContext);
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

        if (TenantContextHolder.isTenantEnabled() && ObjectUtil.isNotNull(sysUserEntity.getTenantId())) {
            // 二次赋值，以防万一（也许前端没有传租户ID，上方的 if 块并没有执行）
            tenantContext = this.checkAndGetTenantContext(sysUserEntity.getTenantId());
            TenantContextHolder.setTenantContext(tenantContext);
        }

        try {
            this.getBaseMapper().updateLastLoginAt(sysUserEntity.getId(), LocalDateTimeUtil.now());
        } catch (Exception ignored) {
            // 实际开发环境请删除本try-catch块
        }

        // 取账号完整BO
        SysUserBO sysUserBO = this.entity2BO(sysUserEntity);

        SysUserLoginBO ret = new SysUserLoginBO();
        BeanUtil.copyProperties(sysUserBO, ret);

        // 因字段类型不一致, 单独转换
        ret
                .setRoleIds(sysUserBO.getRoleMap().keySet())
                .setRoles(sysUserBO.getRoleMap().values())
                .setPermissions(sysMenuService.listPermissionByRoleIds(sysUserBO.getRoleMap().keySet()))
                .setTenantContext(tenantContext)
        ;

        return ret;
    }

    /**
     * 后台管理-取当前用户信息
     */
    public VbenAdminUserInfoBO adminGetCurrentUserInfo() {
        SysUserBO sysUserBO = this.getOneById(UserContextHolder.getUserId(), true);
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
     * 后台管理-绑定用户与角色关联关系
     */
    public void adminBindRoles(AdminBindUserRoleRelationDTO dto) {
        sysUserRoleRelationService.cleanAndBind(dto.getUserId(), dto.getRoleIds());
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public SysUserBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public SysUserBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        SysUserEntity entity = this.getById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 根据用户账号查询
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
     * 取用户基本信息
     */
    public SysUserBaseInfoBO getBaseInfoById(Long entityId) {
        return this.getBaseMapper().getBaseInfoByUserId(entityId);
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
    private SysUserBO entity2BO(SysUserEntity entity) {
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

        return bo;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @return BO List
     */
    private List<SysUserBO> entityList2BOs(List<SysUserEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<SysUserBO> ret = new ArrayList<>(entityList.size());
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
    private PageResult<SysUserBO> entityPage2BOPage(Page<SysUserEntity> entityPage) {
        PageResult<SysUserBO> ret = new PageResult<>();
        BeanUtil.copyProperties(entityPage, ret);
        ret.setRecords(this.entityList2BOs(entityPage.getRecords()));

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

    /**
     * 检查并获取租户上下文 bean，无效或被禁用则直接抛出异常
     * @param tenantId 租户ID
     * @return TenantContext
     */
    private TenantContext checkAndGetTenantContext(Long tenantId) throws BusinessException {
        // 查询所属租户是否仍有效，这里是直接查库，可以自行改造为从缓存读取
        SysTenantBO tenantInfo = sysTenantService.getTenantByTenantId(tenantId);
        if (tenantInfo == null) {
            throw new BusinessException(SysErrorEnum.INVALID_TENANT);
        }

        if (GenericStatusEnum.DISABLED.equals(tenantInfo.getStatus())) {
            throw new BusinessException(SysErrorEnum.DISABLED_TENANT);
        }

        return TenantContext.builder()
                .tenantId(tenantInfo.getTenantId())
                .tenantName(tenantInfo.getTenantName())
                .build()
        ;
    }
}
