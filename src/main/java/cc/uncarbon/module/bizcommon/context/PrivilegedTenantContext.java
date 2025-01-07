package cc.uncarbon.module.bizcommon.context;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.TenantContext;

/**
 * 快速构造一个超级租户实例，用于临时绕过租户拦截器；同时提供一个单例对象
 * 使用示例
 * <p>
 *     TenantContext originContext = TenantContextHolder.getTenantContext();
 *     try {
 *         TenantContextHolder.setTenantContext(PrivilegedTenantContext.SINGLETON);
 *         // 业务代码...
 *     } finally {
 *         TenantContextHolder.setTenantContext(originContext);
 *     }
 * </p>
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
