package cc.uncarbon.module.sys.controller;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.model.request.*;
import cc.uncarbon.module.sys.model.response.SysUserBO;
import cc.uncarbon.module.sys.model.response.VbenAdminUserInfoBO;
import cc.uncarbon.module.sys.service.SysUserService;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * @author Uncarbon
 */
@RequiredArgsConstructor
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Slf4j
@Api(value = "后台用户管理接口", tags = {"后台用户管理接口"})
@RequestMapping(SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/sys/users")
@RestController
public class AdminSysUserController {

    private static final String PERMISSION_PREFIX = "SysUser:";

    private final SysUserService sysUserService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "分页列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public ApiResult<PageResult<SysUserBO>> list(PageParam pageParam, AdminListSysUserDTO dto) {
        return ApiResult.data(sysUserService.adminList(pageParam, dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public ApiResult<SysUserBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysUserService.getOneById(id, true));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping
    public ApiResult<?> insert(@RequestBody @Valid AdminInsertOrUpdateSysUserDTO dto) {
        sysUserService.adminInsert(dto);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public ApiResult<?> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateSysUserDTO dto) {
        dto.setId(id);
        sysUserService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping
    public ApiResult<?> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysUserService.adminDelete(dto.getIds());

        return ApiResult.success();
    }

    @ApiOperation(value = "取当前用户信息", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/info")
    public ApiResult<VbenAdminUserInfoBO> getCurrentUserInfo() {
        return ApiResult.data(sysUserService.adminGetCurrentUserInfo());
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = "SysUser:resetPassword")
    @ApiOperation(value = "重置某用户密码", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/resetPassword")
    public ApiResult<?> resetPassword(@RequestBody @Valid AdminResetSysUserPasswordDTO dto) {
        sysUserService.adminResetUserPassword(dto);

        // 踢出原登录
        AdminStpUtil.kickout(dto.getUserId());

        return ApiResult.success();
    }

    @ApiOperation(value = "修改当前用户密码", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/updatePassword")
    public ApiResult<?> updatePassword(@RequestBody @Valid AdminUpdateCurrentSysUserPasswordDTO dto) {
        if (StrUtil.isBlank(dto.getNewPassword()) || StrUtil.isBlank(dto.getConfirmNewPassword())) {
            throw new BusinessException(400, "密码或确认密码不能为空");
        }

        if (!dto.getConfirmNewPassword().equals(dto.getNewPassword())) {
            throw new BusinessException(400, "密码与确认密码不同，请检查");
        }
        sysUserService.adminUpdateCurrentUserPassword(dto);

        // 用户更改密码后使其当前会话直接过期
        AdminStpUtil.logout();

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = "SysUser:bindRoles")
    @ApiOperation(value = "绑定用户与角色关联关系", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/bindRoles")
    public ApiResult<?> bindRoles(@RequestBody @Valid AdminBindUserRoleRelationDTO dto) {
        sysUserService.adminBindRoles(dto);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = "SysUser:kickOut")
    @ApiOperation(value = "踢某用户下线", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/kickOut")
    public ApiResult<?> kickOut(@RequestBody @Valid AdminKickOutSysUserDTO dto) {
        AdminStpUtil.kickout(dto.getUserId());

        return ApiResult.success();
    }

}
