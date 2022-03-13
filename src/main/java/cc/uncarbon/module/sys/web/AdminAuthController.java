package cc.uncarbon.module.sys.web;


import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.TenantContext;
import cc.uncarbon.framework.core.context.TenantContextHolder;
import cc.uncarbon.framework.core.context.UserContext;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.helper.RolePermissionCacheHelper;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.enums.UserTypeEnum;
import cc.uncarbon.module.sys.model.request.SysUserLoginDTO;
import cc.uncarbon.module.sys.model.response.SysUserLoginBO;
import cc.uncarbon.module.sys.model.response.SysUserLoginVO;
import cc.uncarbon.module.sys.service.SysUserService;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Uncarbon
 */
@RequiredArgsConstructor
@Slf4j
@Api(value = "SaaS后台管理鉴权接口", tags = {"SaaS后台管理鉴权接口"})
@RequestMapping(SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/auth")
@RestController
public class AdminAuthController {

    private final SysUserService sysUserService;

    private final RolePermissionCacheHelper rolePermissionCacheHelper;


    @ApiOperation(value = "登录", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/login")
    public ApiResult<SysUserLoginVO> login(@RequestBody @Valid SysUserLoginDTO dto) {
        // RPC调用, 失败抛异常, 成功返回用户信息
        SysUserLoginBO userInfo = sysUserService.adminLogin(dto);

        // 构造用户上下文
        UserContext userContext = UserContext.builder()
                .userId(userInfo.getId())
                .userName(userInfo.getUsername())
                .userPhoneNo(userInfo.getPhoneNo())
                .userType(UserTypeEnum.ADMIN_USER)
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
    @ApiOperation(value = "登出", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/logout")
    public ApiResult<?> logout() {
        AdminStpUtil.logout();
        UserContextHolder.setUserContext(null);
        TenantContextHolder.setTenantContext(null);

        return ApiResult.success();
    }

}
