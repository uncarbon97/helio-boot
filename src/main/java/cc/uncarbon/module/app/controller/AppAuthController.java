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
@RequestMapping(AppConstant.APP_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/auth")
@RestController
public class AppAuthController {

    @ApiOperation(value = "登录", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/login")
    public ApiResult<?> login() {
        /*
        编码时请参考AdminAuthController#login
         */

        return ApiResult.success();
    }

}
