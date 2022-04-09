package cc.uncarbon.config;

import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.helper.RolePermissionCacheHelper;
import cn.dev33.satoken.stp.StpInterface;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 自定义权限验证接口扩展
 * 用于后台管理
 *
 * @author Uncarbon
 */
@Component
@RequiredArgsConstructor
public class AdminSaTokenExtendConfiguration implements StpInterface {

    private final RolePermissionCacheHelper rolePermissionCacheHelper;


    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return rolePermissionCacheHelper.getUserPermissions();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return UserContextHolder.getUserContext().getRoles();
    }
}
