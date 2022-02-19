package cc.uncarbon.interceptor;


import cc.uncarbon.framework.core.props.HelioProperties;
import cc.uncarbon.framework.tenant.resolver.TenantHeaderResolver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 从请求头解析租户信息，并赋值到上下文
 *
 * @author Uncarbon
 */
@Slf4j
@RequiredArgsConstructor
public class TenantHeaderResolveInterceptor implements AsyncHandlerInterceptor {

    private final HelioProperties helioProperties;

    private final TenantHeaderResolver tenantHeaderResolver;


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (handler instanceof ResourceHttpRequestHandler) {
            // 直接放行静态资源
            return true;
        }

        // 例子见 SampleTenantHeaderResolver.java
        tenantHeaderResolver.resolve(request, response, handler, helioProperties);

        return true;
    }
}
