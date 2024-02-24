package cc.uncarbon.interceptor;

import cc.uncarbon.framework.core.context.TenantContext;
import cc.uncarbon.framework.core.context.TenantContextHolder;
import cc.uncarbon.framework.core.context.UserContext;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.web.util.IPUtil;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cn.dev33.satoken.session.SaSession;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 从请求头解析并赋值到用户上下文，用于后台管理用户的鉴权
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
            setContextsFromSaSession(AdminStpUtil.getSession(), request);
            if (log.isDebugEnabled()) {
                log.debug("[SA-Token][Admin] 从请求头解析出用户上下文 >> {}", UserContextHolder.getUserContext());
            }
        } else {
            UserContextHolder.clear();
            TenantContextHolder.clear();
        }

        return true;
    }

    /**
     * 自 SaSession 更新当前线程用户、租户上下文
     *
     * @param session 会话对象
     * @param request ServletRequest 对象
     */
    public static void setContextsFromSaSession(SaSession session, HttpServletRequest request) {
        // 赋值用户上下文
        UserContext userContext = (UserContext) session.get(UserContext.CAMEL_NAME);

        // 获取用户公网IP
        // 先按逗号分隔后，再取第index个IP地址（从0开始）；兼容启用了云防护盾CDN的服务器（可能获取到的IP会带上中间代理节点的IP地址）
        userContext.setClientIP(IPUtil.getClientIPAddress(request, 0));
        UserContextHolder.setUserContext(userContext);

        // 赋值租户上下文
        TenantContext tenantContext = (TenantContext) session.get(TenantContext.CAMEL_NAME);
        TenantContextHolder.setTenantContext(tenantContext);
    }

}
