package cc.uncarbon.module.adminapi.web.sys;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.enums.SysUserStatusEnum;
import cc.uncarbon.module.sys.model.request.*;
import cc.uncarbon.module.sys.model.response.SysUserBO;
import cc.uncarbon.module.sys.model.response.VbenAdminUserInfoVO;
import cc.uncarbon.module.sys.service.SysUserService;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;


@SaCheckLogin(type = AdminStpUtil.TYPE)
@Api(value = "后台用户管理接口", tags = {"后台用户管理接口"})
@RequestMapping(value = {
        // 兼容旧的API路由前缀
        SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1,
        AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1"
})
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminSysUserController {

    private static final String PERMISSION_PREFIX = "SysUser:";

    private final SysUserService sysUserService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/sys/users")
    public ApiResult<PageResult<SysUserBO>> list(PageParam pageParam, AdminListSysUserDTO dto) {
        return ApiResult.data(sysUserService.adminList(pageParam, dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情")
    @GetMapping(value = "/sys/users/{id}")
    public ApiResult<SysUserBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysUserService.getOneById(id, true));
    }

    @SysLog(value = "新增后台用户")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增")
    @PostMapping(value = "/sys/users")
    public ApiResult<Void> insert(@RequestBody @Valid AdminInsertOrUpdateSysUserDTO dto) {
        sysUserService.adminInsert(dto);

        return ApiResult.success();
    }

    @SysLog(value = "编辑后台用户")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑")
    @PutMapping(value = "/sys/users/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateSysUserDTO dto) {
        dto.setId(id);
        sysUserService.adminUpdate(dto);

        // 新状态是禁用，连带踢出登录
        if (dto.getStatus() == SysUserStatusEnum.BANNED) {
            kickOut(dto.getId());
        }

        return ApiResult.success();
    }

    @SysLog(value = "删除后台用户")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/sys/users")
    public ApiResult<Void> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysUserService.adminDelete(dto.getIds());

        // 连带踢出登录
        dto.getIds().forEach(this::kickOut);

        return ApiResult.success();
    }

    @ApiOperation(value = "取当前用户信息")
    @GetMapping(value = "/sys/users/info")
    public ApiResult<VbenAdminUserInfoVO> getCurrentUserInfo() {
        return ApiResult.data(sysUserService.adminGetCurrentUserInfo());
    }

    @SysLog(value = "重置某用户密码")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + "resetPassword")
    @ApiOperation(value = "重置某用户密码")
    @PutMapping(value = "/sys/users/{userId}/password")
    public ApiResult<Void> resetPassword(@PathVariable Long userId, @RequestBody @Valid AdminResetSysUserPasswordDTO dto) {
        dto.setUserId(userId);
        sysUserService.adminResetUserPassword(dto);

        // 踢出原登录
        AdminStpUtil.kickout(dto.getUserId());

        return ApiResult.success();
    }

    @SysLog(value = "修改当前用户密码")
    @ApiOperation(value = "修改当前用户密码")
    @PostMapping(value = {
            "/sys/users/me/password:update",
            // 兼容旧的API路由
            "/sys/users/updatePassword"
    })
    public ApiResult<Void> updatePassword(@RequestBody @Valid AdminUpdateCurrentSysUserPasswordDTO dto) {
        if (!dto.getConfirmNewPassword().equals(dto.getNewPassword())) {
            throw new BusinessException(400, "密码与确认密码不同，请检查");
        }
        sysUserService.adminUpdateCurrentUserPassword(dto);

        // 用户更改密码后使其当前会话直接过期
        AdminStpUtil.logout();

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + "bindRoles")
    @ApiOperation(value = "绑定用户与角色关联关系")
    @PutMapping(value = "/sys/users/{userId}/roles")
    public ApiResult<Void> bindRoles(@PathVariable Long userId, @RequestBody AdminBindUserRoleRelationDTO dto) {
        dto.setUserId(userId);
        sysUserService.adminBindRoles(dto);

        // 该用户会被强制踢下线，以更新对应权限；可以视业务需要决定是否删除该代码
        this.kickOut(dto.getUserId());

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + "kickOut")
    @ApiOperation(value = "踢某用户下线")
    @PostMapping(value = {
            "/sys/users/{userId}:kick-out",
            // 兼容旧的API路由
            "/sys/users/{userId}/kickOut"
    })
    public ApiResult<Void> kickOut(@PathVariable Long userId) {
        AdminStpUtil.kickout(userId);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "取指定用户关联角色ID")
    @GetMapping(value = {
            "/sys/users/{userId}/roles",
            // 兼容旧的API路由
            "/sys/users/{userId}/relatedRoleIds"
    })
    public ApiResult<Set<Long>> listRelatedRoleIds(@PathVariable Long userId) {
        return ApiResult.data(sysUserService.listRelatedRoleIds(userId));
    }

}
