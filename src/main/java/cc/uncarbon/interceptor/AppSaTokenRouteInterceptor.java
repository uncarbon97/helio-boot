package cc.uncarbon.interceptor;

import cc.uncarbon.framework.core.props.HelioProperties;
import cc.uncarbon.module.app.constant.AppConstant;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * 路由拦截器，自定义验证规则
 * http://sa-token.dev33.cn/doc/index.html#/use/route-check
 * @author Uncarbon
 */
@RequiredArgsConstructor
public class AppSaTokenRouteInterceptor extends SaRouteInterceptor {

    private final HelioProperties helioProperties;


    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, ModelAndView modelAndView) {
        /*
        App登录验证
        这里是按路由匹配，除了配置文件中指定的URI外，其他URI都需要登录才行
         */
        SaRouter
                .match(Collections.singletonList(AppConstant.APP_MODULE_CONTEXT_PATH + "/**"))
                .notMatch(helioProperties.getSecurity().getExcludeRoutes())
                .check(StpUtil::checkLogin)
                ;
    }
}
