package cc.uncarbon.interceptor;

import cc.uncarbon.framework.core.context.TenantContext;
import cc.uncarbon.framework.core.context.TenantContextHolder;
import cc.uncarbon.framework.core.context.UserContext;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.web.util.IPUtil;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

        // 从请求头解析用户上下文
        if (AdminStpUtil.isLogin()) {
            UserContext currentUser = (UserContext) AdminStpUtil.getSession().get(UserContext.CAMEL_NAME);
            log.debug("[SA-Token][Admin] 从请求头解析出用户上下文 >> {}", currentUser);

            currentUser
                    .setClientIP(IPUtil.getClientIPAddress(request))
            ;
            UserContextHolder.setUserContext(currentUser);

            // 赋值对应租户上下文
            if (TenantContextHolder.isTenantEnabled()) {
                // 启用了多租户的前提下，才获取
                TenantContext tenantContext = (TenantContext) AdminStpUtil.getSession().get(TenantContext.CAMEL_NAME);
                TenantContextHolder.setTenantContext(tenantContext);
            }

        } else {
            UserContextHolder.setUserContext(null);
            TenantContextHolder.setTenantContext(null);
        }

        return true;
    }

}
