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
     * 缓存键名-侧边菜单-前缀
     */
    String REDIS_KEY_SIDE_MENU = "sys-service:sideMenu:";

    /**
     * 缓存键名-侧边菜单-区分用户ID
     */
    String REDIS_KEY_SIDE_MENU_BY_USERID = REDIS_KEY_SIDE_MENU + "userId_%s";

    /**
     * 缓存键名-所有可见菜单-前缀
     */
    String REDIS_KEY_VISIBLE_MENU = "sys-service:visibleMenu:";

    /**
     * 缓存键名-所有可见菜单-区分用户ID
     */
    String REDIS_KEY_VISIBLE_MENU_BY_USERID = REDIS_KEY_VISIBLE_MENU + "userId_%s";

    /**
     * 参数键名-缓存菜单时长
     */
    String PARAM_KEY_CACHE_MENU_DURATION = "sys:cache-menu-duration";

    /**
     * 缓存键名前缀-用户业务缓存
     */
    String CACHE_KEY_USER_BIZ_CACHE = "sys-service:userBizCache:%s";

    /**
     * 缓存键名-用户拥有角色ID
     */
    String REDIS_KEY_USER_OWNED_ROLE_IDS = CACHE_KEY_USER_BIZ_CACHE + ":ownedRoleIds";

    /**
     * 一整天有多少秒
     */
    long ONE_DAY_SECONDS = 86400;

}
