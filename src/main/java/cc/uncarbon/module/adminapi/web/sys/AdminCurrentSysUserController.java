package cc.uncarbon.module.adminapi.web.sys;

import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.model.request.AdminUpdateCurrentSysUserAvatarDTO;
import cc.uncarbon.module.sys.model.request.AdminUpdateCurrentSysUserInfoDTO;
import cc.uncarbon.module.sys.model.request.AdminUpdateCurrentSysUserPasswordDTO;
import cc.uncarbon.module.sys.model.response.VbenAdminUserInfoVO;
import cc.uncarbon.module.sys.service.SysUserService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@SaCheckLogin(type = AdminStpUtil.TYPE)
@Tag(name = "当前后台用户信息接口")
@RequestMapping(value = AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminCurrentSysUserController {

    private final SysUserService sysUserService;


    @Operation(summary = "取当前用户信息资料")
    // 新路由与旧路由并行
    @GetMapping(value = {"/sys/users/me/info", "/sys/users/info"})
    public ApiResult<VbenAdminUserInfoVO> getMyInfo() {
        return ApiResult.data(sysUserService.adminGetCurrentUserInfo());
    }

    @SysLog(value = "修改当前用户密码")
    @Operation(summary = "修改当前用户密码")
    @PostMapping(value = "/sys/users/me/password:update")
    public ApiResult<Void> updatePassword(@RequestBody @Valid AdminUpdateCurrentSysUserPasswordDTO dto) {
        if (!dto.getConfirmNewPassword().equals(dto.getNewPassword())) {
            throw new BusinessException(400, "密码与确认密码不同，请检查");
        }
        sysUserService.adminUpdateCurrentUserPassword(dto);

        // 用户更改密码后使其当前会话直接过期
        AdminStpUtil.logout();

        return ApiResult.success();
    }

    @Operation(summary = "更新当前用户信息资料")
    @PutMapping(value = "/sys/users/me/info")
    public ApiResult<Void> updateMyInfo(@RequestBody @Valid AdminUpdateCurrentSysUserInfoDTO dto) {
        sysUserService.adminUpdateCurrentUserInfo(dto);
        return ApiResult.success();
    }

    @Operation(summary = "更新当前用户头像")
    @PutMapping(value = "/sys/users/me/avatar")
    public ApiResult<Void> updateMyAvatar(@RequestBody @Valid AdminUpdateCurrentSysUserAvatarDTO dto) {
        sysUserService.adminUpdateCurrentUserAvatar(dto);
        return ApiResult.success();
    }

}
