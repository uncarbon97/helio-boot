package cc.uncarbon.module.sys.constant;


/**
 * 系统管理常量
 */
public interface SysConstant {

    /**
     * 后台管理接口路由前缀
     * @deprecated since 1.9.0, because its meaning is no longer so fitting; replaced by AdminApiConstant#API_URL_PREFIX
     */
    @Deprecated
    String SYS_MODULE_CONTEXT_PATH = "/sys";

    /**
     * 无上级节点的父级ID
     */
    Long ROOT_PARENT_ID = 0L;

    /**
     * Vben Admin后台管理-空页面
     */
    String VBEN_ADMIN_BLANK_VIEW = "LAYOUT";

    /**
     * 超级管理员角色ID
     */
    Long SUPER_ADMIN_ROLE_ID = 1L;

    /**
     * 敏感字段，`SysLogAspect` 切面记录系统操作日志时，会先去除敏感字段后再入库
     */
    String[] SENSITIVE_FIELDS = {"password", "oldPassword", "newPassword", "confirmNewPassword", "passwordOfNewUser", "randomPassword", "tenantAdminPassword"};

}
