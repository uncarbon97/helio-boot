package cc.uncarbon.module.sys.web.auth;


import cc.uncarbon.aspect.extension.SysLogAspectExtensionForSysUserLogin;
import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.TenantContext;
import cc.uncarbon.framework.core.context.TenantContextHolder;
import cc.uncarbon.framework.core.context.UserContext;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.helper.CaptchaHelper;
import cc.uncarbon.helper.RolePermissionCacheHelper;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.model.request.SysUserLoginDTO;
import cc.uncarbon.module.sys.model.response.SysUserLoginBO;
import cc.uncarbon.module.sys.model.response.SysUserLoginVO;
import cc.uncarbon.module.sys.service.SysUserService;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.captcha.AbstractCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
@Api(value = "SaaS后台管理鉴权接口", tags = {"SaaS后台管理鉴权接口"})
@RequestMapping(SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/auth")
@RestController
public class AdminAuthController {

    private final SysUserService sysUserService;

    private final RolePermissionCacheHelper rolePermissionCacheHelper;

    private final CaptchaHelper captchaHelper;


    @SysLog(value = "登录后台用户", syncSave = true, extension = SysLogAspectExtensionForSysUserLogin.class)
    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public ApiResult<SysUserLoginVO> login(@RequestBody @Valid SysUserLoginDTO dto) {
        // RPC调用, 失败抛异常, 成功返回用户信息
        SysUserLoginBO userInfo = sysUserService.adminLogin(dto);

        // 构造用户上下文
        UserContext userContext = UserContext.builder()
                .userId(userInfo.getId())
                .userName(userInfo.getUsername())
                .userPhoneNo(userInfo.getPhoneNo())
                .userTypeStr("ADMIN_USER")
                .extraData(null)
                .rolesIds(userInfo.getRoleIds())
                .roles(userInfo.getRoles())
                .build();

        // 将用户ID注册到 SA-Token ，并附加一些业务字段
        AdminStpUtil.login(userInfo.getId(), dto.getRememberMe());
        AdminStpUtil.getSession().set(UserContext.CAMEL_NAME, userContext);
        AdminStpUtil.getSession().set(TenantContext.CAMEL_NAME, userInfo.getTenantContext());

        // 更新角色-权限缓存
        rolePermissionCacheHelper.putCache(userInfo.getRoleIdPermissionMap());

        // 返回登录token
        SysUserLoginVO tokenInfo = SysUserLoginVO.builder()
                .tokenName(AdminStpUtil.getTokenName())
                .tokenValue(AdminStpUtil.getTokenValue())
                .roles(userInfo.getRoles())
                .permissions(userInfo.getPermissions())
                .build();

        return ApiResult.data("登录成功", tokenInfo);
    }

    @SaCheckLogin(type = AdminStpUtil.TYPE)
    @ApiOperation(value = "登出")
    @PostMapping(value = "/logout")
    public ApiResult<?> logout() {
        AdminStpUtil.logout();
        UserContextHolder.clear();
        TenantContextHolder.clear();

        return ApiResult.success();
    }

    @ApiOperation(value = "验证码图片")
    @ApiImplicitParam(name = "uuid", value = "验证码图片UUID", required = true)
    @GetMapping(value = "/captcha")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        /*
        由前端定义 UUID 其实并不算太好的办法，但是够简单
        更复杂而安全的做法是：由后端生成一个 UUID，通过响应头返回给前端（这对前端有一定的技能技术要求）
         */
        // uuid 为空则抛出异常
        SysErrorEnum.UUID_CANNOT_BE_BLANK.assertNotBlank(uuid);

        // 核验方法：captchaHelper.validate;
        AbstractCaptcha captcha = captchaHelper.generate(uuid);

        // 写入响应流
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/png");
        captcha.write(response.getOutputStream());
    }

}
