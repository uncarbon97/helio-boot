package cc.uncarbon.module.sys.constant;


/**
 * 系统管理常量
 */
public final class SysConstant {
    private SysConstant() {
    }

    /**
     * 后台管理接口路由前缀
     * @deprecated since 1.9.0, because its meaning is no longer so fitting; replaced by AdminApiConstant#API_URL_PREFIX
     */
    @Deprecated
    public static final String SYS_MODULE_CONTEXT_PATH = "/sys";

    /**
     * 无上级节点的父级ID
     */
    public static final Long ROOT_PARENT_ID = 0L;

    /**
     * Vben Admin后台管理-空页面
     */
    public static final String VBEN_ADMIN_BLANK_VIEW = "LAYOUT";

    /**
     * 超级管理员角色ID
     */
    public static final Long SUPER_ADMIN_ROLE_ID = 1L;

}
