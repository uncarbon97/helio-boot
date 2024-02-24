package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * sys模块错误枚举类
 */
@AllArgsConstructor
@Getter
public enum SysErrorEnum implements HelioBaseEnum<Integer> {

    INVALID_ID(400, "无效ID"),

    INCORRECT_PIN_OR_PWD(400, "账号或密码不正确"),

    BANNED_USER(400, "用户被封禁"),

    INVALID_TENANT(400, "所属租户无效"),

    DISABLED_TENANT(400, "所属租户已禁用"),

    INCORRECT_OLD_PASSWORD(400, "原密码有误"),

    NO_ROLE_AVAILABLE_FOR_CURRENT_USER(400, "当前用户没有可用角色"),

    NO_MENU_AVAILABLE_FOR_CURRENT_ROLE(400, "当前角色没有可用菜单"),

    /*
    以下8个枚举用于后台角色管理的越权检查
    */

    ROLE_VALUE_CANNOT_BE(403, "角色值 {} 不能用于新增或编辑，请选用其他值"),

    CANNOT_DELETE_SUPER_ADMIN_ROLE(403, "不能删除超级管理员角色"),

    CANNOT_DELETE_TENANT_ADMIN_ROLE(403, "为减少脏数据，不建议直接删除租户管理员角色，需通过【删除租户】关联删除"),

    CANNOT_DELETE_SELF_ROLE(403, "不能删除自身角色"),

    CANNOT_BIND_MENUS_FOR_SUPER_ADMIN_ROLE(403, "不能为超级管理员角色绑定菜单"),

    CANNOT_BIND_MENUS_FOR_SELF(403, "不能为自身角色绑定菜单"),

    BEYOND_AUTHORITY_BIND_MENUS(401, "不得超越自身菜单权限"),

    CANNOT_BIND_MENUS_FOR_TENANT_ADMIN_ROLE(403, "无权为租户管理员绑定菜单"),

    /*
    以下4个枚举用于后台用户管理的越权检查
    */
    CANNOT_OPERATE_SELF_USER(403, "不能对自身进行此操作"),

    CANNOT_OPERATE_THIS_USER(403, "不能该用户进行此操作"),

    CANNOT_UNBIND_SELF_TENANT_ADMIN_ROLE(403, "自身的管理员角色不能被取消"),

    BEYOND_AUTHORITY_BIND_ROLES(401, "不得超越自身角色权限"),

    CANNOT_DELETE_PRIVILEGED_TENANT(403, "不能删除超级租户"),

    NEED_DELETE_EXISTING_TENANT_ADMIN_ROLE(500, "租户ID {} 对应的租户管理员角色已存在，请使用超级管理员账号删除"),

    ;
    private final Integer value;
    private final String label;

}
