package cc.uncarbon.module.app.web;


import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.app.constant.AppConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Uncarbon
 */
@RequiredArgsConstructor
@Slf4j
@Api(value = "APP鉴权接口", tags = {"APP鉴权接口"})
@RequestMapping(AppConstant.APP_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/auth")
@RestController
public class AppAuthController {

    /*
    /app/** 开头的C端接口默认为都需要登录，放行接口请在配置文件的 helio.security.exclude-routes 中设置
    相关拦截器代码请见 CustomInterceptorConfiguration.java
     */

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ApiResult<?> login() {
        /*
        编码时请参考AdminAuthController#login
         */

        return ApiResult.success();
    }

}
