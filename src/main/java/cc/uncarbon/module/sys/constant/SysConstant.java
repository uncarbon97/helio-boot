package cc.uncarbon.module.sys.constant;


/**
 * @author Uncarbon
 */
public interface SysConstant {

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
