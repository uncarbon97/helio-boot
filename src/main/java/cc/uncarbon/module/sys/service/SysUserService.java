package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.context.TenantContext;
import cc.uncarbon.framework.core.context.TenantContextHolder;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.core.props.HelioProperties;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.entity.SysTenantEntity;
import cc.uncarbon.module.sys.entity.SysUserEntity;
import cc.uncarbon.module.sys.enums.GenericStatusEnum;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.enums.SysUserStatusEnum;
import cc.uncarbon.module.sys.mapper.SysUserMapper;
import cc.uncarbon.module.sys.model.request.*;
import cc.uncarbon.module.sys.model.response.SysDeptBO;
import cc.uncarbon.module.sys.model.response.SysUserBO;
import cc.uncarbon.module.sys.model.response.SysUserLoginBO;
import cc.uncarbon.module.sys.model.response.VbenAdminUserInfoVO;
import cc.uncarbon.module.sys.util.PwdUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
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

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;


/**
 * 后台用户
 *
 * @author Uncarbon
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserService extends HelioBaseServiceImpl<SysUserMapper, SysUserEntity> {

    private final SysRoleService sysRoleService;

    private final SysDeptService sysDeptService;

    private final SysMenuService sysMenuService;

    private final SysTenantService sysTenantService;

    private final SysUserDeptRelationService sysUserDeptRelationService;

    private final SysUserRoleRelationService sysUserRoleRelationService;

    private final HelioProperties helioProperties;

    private boolean isTenantEnabled;

    @PostConstruct
    public void postConstruct() {
        this.isTenantEnabled = helioProperties.getTenant().getEnabled();
    }

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
     * 后台管理-新增
     *
     * @return 主键ID
     */
    @SysLog(value = "新增后台用户")
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysUserDTO dto) {
        log.info("[后台管理-新增后台用户] >> 入参={}", dto);
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
        log.info("[后台管理-编辑后台用户] >> 入参={}", dto);
        this.checkExistence(dto);

        SysUserEntity updateEntity = new SysUserEntity();
        BeanUtil.copyProperties(dto, updateEntity);
        // 手动处理异名字段
        updateEntity.setPin(dto.getUsername());

        sysUserDeptRelationService.cleanAndBind(dto.getId(), dto.getDeptId());

        this.updateById(updateEntity);
    }

    /**
     * 后台管理-删除
     */
    @SysLog(value = "删除后台用户")
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除后台用户] >> 入参={}", ids);
        this.removeByIds(ids);
    }

    /**
     * 后台管理-登录
     */
    @SysLog(value = SysConstant.SysLogOperation.SYS_USER_LOGIN)
    public SysUserLoginBO adminLogin(SysUserLoginDTO dto) {
        /*
        如果启用了多租户功能，并且前端指定了租户ID，则先查库确认租户是否有效

        注意：数据源级多租户，登录前【必须】主动指定租户ID，如: dto.setTenantId(101L);
         */
        // ConcurrentHashMap 的 value 不能为 null，还是 new 一个吧
        TenantContext tenantContext = new TenantContext();
        if (isTenantEnabled && ObjectUtil.isNotNull(dto.getTenantId())) {
            tenantContext = this.checkAndGetTenantContext(dto.getTenantId());
            // 验证通过，将所属租户写入租户上下文，使得 SQL 拦截器可以正确执行
            TenantContextHolder.setTenantContext(tenantContext);
        }

        // 不要直接提示“账号不存在”或“密码不正确”，避免撞库攻击
        SysUserEntity sysUserEntity = this.getUserByPin(dto.getUsername());
        if (sysUserEntity == null) {
            throw new BusinessException(SysErrorEnum.INCORRECT_PIN_OR_PWD);
        }

        if (!PwdUtil.encrypt(dto.getPassword(), sysUserEntity.getSalt()).equals(sysUserEntity.getPwd())) {
            throw new BusinessException(SysErrorEnum.INCORRECT_PIN_OR_PWD);
        }

        if (SysUserStatusEnum.BANNED.equals(sysUserEntity.getStatus())) {
            throw new BusinessException(SysErrorEnum.BANNED_USER);
        }

        /*
        以上为有效性校验, 进入实际业务逻辑
        ---------------------------------------------------
         */

        if (isTenantEnabled && ObjectUtil.isNotNull(sysUserEntity.getTenantId())) {
            // 二次赋值，以防万一（也许前端没有传租户ID，上方的 if 块并没有执行）
            tenantContext = this.checkAndGetTenantContext(sysUserEntity.getTenantId());
            TenantContextHolder.setTenantContext(tenantContext);
        }

        this.updateLastLoginAt(sysUserEntity.getId(), LocalDateTimeUtil.now());

        // 取账号完整信息
        SysUserBO sysUserBO = this.entity2BO(sysUserEntity);
        Map<Long, String> roleMap = sysRoleService.getRoleMapByUserId(sysUserBO.getId());

        Map<Long, Set<String>> roleIdPermissionMap = sysMenuService.getRoleIdPermissionMap(roleMap.keySet());

        // 包装返回体；有的字段类型不一致, 单独转换
        SysUserLoginBO ret = new SysUserLoginBO();
        BeanUtil.copyProperties(sysUserBO, ret);

        // aka * 64
        HashSet<String> permissions = new HashSet<>(roleIdPermissionMap.size() << 6);
        roleIdPermissionMap.values().forEach(permissions::addAll);

        ret
                .setRoleIds(new HashSet<>(roleMap.keySet()))
                .setRoles(new ArrayList<>(roleMap.values()))
                .setPermissions(permissions)
                .setRoleIdPermissionMap(roleIdPermissionMap)
                .setTenantContext(tenantContext)
        ;

        return ret;
    }

    /**
     * 后台管理-取当前用户信息
     */
    public VbenAdminUserInfoVO adminGetCurrentUserInfo() {
        SysUserBO sysUserBO = this.getOneById(UserContextHolder.getUserId(), true);
        return VbenAdminUserInfoVO.builder()
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
     * 根据用户账号查询
     */
    public SysUserEntity getUserByPin(String pin) {
        return this.getBaseMapper().getUserByPin(pin);
    }

    /**
     * 后台管理 - 取指定用户关联角色ID
     * @param userId 用户ID
     * @return 角色Ids
     */
    public Set<Long> listRelatedRoleIds(Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return Collections.emptySet();
        }

        return sysRoleService.getRoleMapByUserId(userId).keySet();
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
        bo.setUsername(entity.getPin());
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
        return new PageResult<SysUserBO>()
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
        // 查询租户是否仍有效
        SysTenantEntity tenantEntity = sysTenantService.getTenantEntityByTenantId(tenantId);
        if (tenantEntity == null) {
            throw new BusinessException(SysErrorEnum.INVALID_TENANT);
        }

        if (GenericStatusEnum.DISABLED.equals(tenantEntity.getStatus())) {
            throw new BusinessException(SysErrorEnum.DISABLED_TENANT);
        }

        return TenantContext.builder()
                .tenantId(tenantEntity.getTenantId())
                .tenantName(tenantEntity.getTenantName())
                .build()
        ;
    }

    private void updateLastLoginAt(Long userId, LocalDateTime lastLoginAt) {
        SysUserEntity entity = new SysUserEntity();
        entity
                .setLastLoginAt(lastLoginAt)
                .setId(userId)
                ;
        this.updateById(entity);
    }
}
