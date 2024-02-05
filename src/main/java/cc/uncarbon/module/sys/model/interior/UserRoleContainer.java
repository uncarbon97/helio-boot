package cc.uncarbon.module.sys.model.interior;

import cc.uncarbon.module.sys.entity.SysRoleEntity;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * 用户关联角色容器
 */
@Getter
public class UserRoleContainer {

    /**
     * 当前用户的角色IDs
     */
    private final Set<Long> currentUserRoleIds;

    /**
     * 当前用户的角色实例集合
     */
    private final List<SysRoleEntity> currentUserRoles;

    /**
     * 为超级管理员
     */
    private final boolean superAdmin;

    /**
     * 为租户管理员
     */
    private final boolean tenantAdmin;

    /**
     * 非级管理员or租户管理员
     */
    private final boolean notAnyAdmin;


    public UserRoleContainer(Set<Long> currentUserRoleIds, List<SysRoleEntity> currentUserRoles) {
        this.currentUserRoleIds = currentUserRoleIds;
        this.currentUserRoles = currentUserRoles;
        this.superAdmin = currentUserRoles.stream().anyMatch(SysRoleEntity::isSuperAdmin);
        this.tenantAdmin = currentUserRoles.stream().anyMatch(SysRoleEntity::isTenantAdmin);
        this.notAnyAdmin = !this.superAdmin && !this.tenantAdmin;
    }
}
