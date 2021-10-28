package cc.uncarbon.module.app.controller;


import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.UserContext;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.app.constant.AppConstant;
import cc.uncarbon.module.sys.enums.UserTypeEnum;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Uncarbon
 */
@Slf4j
@Api(value = "APP鉴权接口", tags = {"APP鉴权接口"})
@RequestMapping(AppConstant.APP_MODULE_CONTEXT_PATH + HelioConstant.Version.APP_API_VERSION_V1 + "/auth")
@RestController
public class AppAuthController {

    @ApiOperation(value = "登录", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/login")
    public ApiResult<?> login() {
        /*
        鉴权方法请参考AdminAuthController.login，自行根据业务实现
         */

        // 构造用户上下文
        UserContext userContext = UserContext.builder()
                .userId(1L)
                .userName("App测试登录")
                .userPhoneNo("12345678910")
                .userType(UserTypeEnum.APP_USER_INDIVIDUAL)
                .extraData(null)
                .roles(CollUtil.newArrayList())
                .permissions(CollUtil.newArrayList())
                .relationalTenant(null)
                .build();

        // 注册到SA-Token
        StpUtil.login(userContext.getUserId());
        StpUtil.getSession().set("userContext", userContext);


        // 返回登录token
        Map<String, Object> tokenInfo = new HashMap<>(16);
        tokenInfo.put("tokenName", StpUtil.getTokenName());
        tokenInfo.put("tokenValue", StpUtil.getTokenValue());
        tokenInfo.put("roles", userContext.getRoles());
        tokenInfo.put("permissions", userContext.getPermissions());

        return ApiResult.data("登录成功", tokenInfo);
    }

}
