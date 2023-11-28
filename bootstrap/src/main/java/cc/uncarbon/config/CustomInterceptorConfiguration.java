package cc.uncarbon.config;

import cc.uncarbon.framework.core.props.HelioProperties;
import cc.uncarbon.interceptor.AdminSaTokenParseInterceptor;
import cc.uncarbon.interceptor.DefaultSaTokenParseInterceptor;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.appapi.constant.AppApiConstant;
import cc.uncarbon.module.sys.constant.SysConstant;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 将自定义拦截器加入到拦截器队列中
 *
 * @author Uncarbon
 */
@Configuration
@RequiredArgsConstructor
public class CustomInterceptorConfiguration implements WebMvcConfigurer {

    private final HelioProperties helioProperties;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
        1. 通用请求头解析，设定用户、租户上下文
         */
        registry
                .addInterceptor(new DefaultSaTokenParseInterceptor())
                // @since 1.7.3 fix: 访问除 /app/** 以外的路由时，不会清除复用线程中残留的用户态
                .addPathPatterns("/**");

        registry
                .addInterceptor(new AdminSaTokenParseInterceptor())
                .addPathPatterns(
                        // 兼容旧的API路由前缀
                        SysConstant.SYS_MODULE_CONTEXT_PATH + "/**",
                        AdminApiConstant.HTTP_API_URL_PREFIX + "/**"
                );

        /*
        2. 注解拦截器，启用注解功能
         */
        registry
                .addInterceptor(new SaInterceptor())
                .addPathPatterns("/**");

        /*
        3. /app/** 路由拦截器, 使几乎所有接口都需要登录
        放行接口请在配置文件的 helio.security.exclude-routes 中设置

        @see https://sa-token.cc/doc.html#/use/route-check
         */
        registry
                .addInterceptor(new SaInterceptor(
                        (handler) -> StpUtil.checkLogin()
                ))
                .addPathPatterns(AppApiConstant.HTTP_API_URL_PREFIX + "/**")
                .excludePathPatterns(helioProperties.getSecurity().getExcludeRoutes());
    }
}
