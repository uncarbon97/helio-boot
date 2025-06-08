package cc.uncarbon.module.bizcommon.context;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.TenantContext;

/**
 * 快速构造一个超级租户上下文，用于临时绕过租户拦截器；同时提供一个单例对象
 * 使用示例
 *     TenantContext originContext = TenantContextHolder.getTenantContext();
 *     try {
 *         if (originContext == null) {
 *             // 用户可能尚未登录，但需要查库（如获取APP首页轮播图等），得绕过租户限制；但如果已经登录了，则不必切换租户态
 *             TenantContextHolder.setTenantContext(PrivilegedTenantContext.SINGLETON);
 *         }
 *         // 业务代码...
 *     } finally {
 *         TenantContextHolder.setTenantContext(originContext);
 *     }
 */
public class PrivilegedTenantContext extends TenantContext {

    public PrivilegedTenantContext() {
        super(HelioConstant.Tenant.DEFAULT_PRIVILEGED_TENANT_ID, "超级租户");
    }

    public static final PrivilegedTenantContext SINGLETON = new PrivilegedTenantContext();

    @Override
    public TenantContext setTenantId(Long tenantId) {
        // 不允许修改
        return this;
    }

    @Override
    public TenantContext setTenantName(String tenantName) {
        // 不允许修改
        return this;
    }
}
