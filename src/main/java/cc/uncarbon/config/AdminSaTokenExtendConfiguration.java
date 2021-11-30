package cc.uncarbon.config;

import cc.uncarbon.framework.core.context.UserContextHolder;
import cn.dev33.satoken.stp.StpInterface;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * 自定义权限验证接口扩展
 * 用于后台管理
 *
 * @author Uncarbon
 */
@Component
public class AdminSaTokenExtendConfiguration implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return UserContextHolder.getUserContext().getPermissions();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return UserContextHolder.getUserContext().getRoles();
    }
}
