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
     * 直接关联的角色IDs
     */
    private final Set<Long> relatedRoleIds;

    /**
     * 直接关联的角色实例集合
     */
    private final List<SysRoleEntity> relatedRoles;

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


    public UserRoleContainer(Set<Long> relatedRoleIds, List<SysRoleEntity> relatedRoles) {
        this.relatedRoleIds = relatedRoleIds;
        this.relatedRoles = relatedRoles;
        this.superAdmin = relatedRoles.stream().anyMatch(SysRoleEntity::isSuperAdmin);
        this.tenantAdmin = relatedRoles.stream().anyMatch(SysRoleEntity::isTenantAdmin);
        this.notAnyAdmin = !this.superAdmin && !this.tenantAdmin;
    }
}
