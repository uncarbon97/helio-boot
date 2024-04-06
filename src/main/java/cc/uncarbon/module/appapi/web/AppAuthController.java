package cc.uncarbon.module.appapi.web;


import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.appapi.constant.AppApiConstant;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@Slf4j
@Tag(name = "APP鉴权接口")
@RequestMapping(AppApiConstant.HTTP_API_URL_PREFIX + "/api/v1")
@RestController
public class AppAuthController {

    /*
    /app/** 开头的C端接口默认为都需要登录，放行接口请在配置文件的 helio.security.exclude-routes 中设置
    相关拦截器代码请见 CustomInterceptorConfiguration.java
     */

    @Operation(summary = "登录")
    @PostMapping("/auth/login")
    public ApiResult<Void> login() {
        // 可参考 admin-api 的 AdminAuthController#login
        return ApiResult.success();
    }

}
