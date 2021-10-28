package cc.uncarbon.config;

import cc.uncarbon.framework.core.props.HelioProperties;
import cc.uncarbon.framework.satoken.interceptor.DefaultSaTokenParseInterceptor;
import cc.uncarbon.interceptor.AdminSaTokenParseInterceptor;
import cc.uncarbon.interceptor.AppSaTokenRouteInterceptor;
import cc.uncarbon.module.app.constant.AppConstant;
import cc.uncarbon.module.sys.constant.SysConstant;
import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


/**
 * 将自定义拦截器加入到拦截器队列中
 * @author Uncarbon
 */
@Configuration
public class CustomInterceptorConfiguration implements WebMvcConfigurer {

    @Resource
    private HelioProperties helioProperties;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
        1. 请求头解析, 设定用户上下文
         */
        registry
                .addInterceptor(new DefaultSaTokenParseInterceptor())
                .addPathPatterns(AppConstant.APP_MODULE_CONTEXT_PATH + "/**");

        registry
                .addInterceptor(new AdminSaTokenParseInterceptor())
                .addPathPatterns(SysConstant.SYS_MODULE_CONTEXT_PATH + "/**");

        /*
        2. App-路由拦截器, 使几乎所有接口都需要登录
         */
        registry
                .addInterceptor(new AppSaTokenRouteInterceptor(helioProperties))
                .addPathPatterns(AppConstant.APP_MODULE_CONTEXT_PATH + "/**")
                .excludePathPatterns(helioProperties.getSecurity().getExcludeRoutes());

        /*
        3. 注解拦截器, 启用注解功能
         */
        registry
                .addInterceptor(new SaAnnotationInterceptor())
                .addPathPatterns("/**");
    }
}
