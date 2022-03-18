package cc.uncarbon.interceptor;

import cc.uncarbon.framework.core.context.TenantContext;
import cc.uncarbon.framework.core.context.TenantContextHolder;
import cc.uncarbon.framework.core.context.UserContext;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.web.util.IPUtil;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;


/**
 * 从请求头解析并赋值到用户上下文
 * 其实就是"DefaultSaTokenParseInterceptor"改个名, 工具类换成"AdminStpUtil"
 * @author Uncarbon
 */
@Slf4j
public class AdminSaTokenParseInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {

        if (handler instanceof ResourceHttpRequestHandler) {
            // 直接放行静态资源
            return true;
        }

        // SA-Token 会自动从请求头中解析 token，所以这里可以直接拿到对应 session，从而取出业务字段
        if (AdminStpUtil.isLogin()) {
            UserContext userContext = (UserContext) AdminStpUtil.getSession().get(UserContext.CAMEL_NAME);

            // 获取用户公网IP
            userContext.setClientIP(IPUtil.getClientIPAddress(request));
            UserContextHolder.setUserContext(userContext);

            log.debug("[SA-Token][Admin] 从请求头解析出用户上下文 >> {}", userContext);

            // 赋值租户上下文
            TenantContext tenantContext = (TenantContext) AdminStpUtil.getSession().get(TenantContext.CAMEL_NAME);
            TenantContextHolder.setTenantContext(tenantContext);

        } else {
            UserContextHolder.setUserContext(null);
            TenantContextHolder.setTenantContext(null);
        }

        return true;
    }

}
