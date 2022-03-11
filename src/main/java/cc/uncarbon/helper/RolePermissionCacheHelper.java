package cc.uncarbon.helper;

import cc.uncarbon.framework.core.context.UserContextHolder;
import cn.hutool.core.collection.CollUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 将角色对应权限，缓存至 Redis
 * 参考文章: https://sa-token.dev33.cn/doc/index.html#/fun/jur-cache
 *
 * @author Uncarbon
 */
@Component
@RequiredArgsConstructor
public class RolePermissionCacheHelper {

    private final RedisTemplate<String, String> stringSetRedisTemplate;

    private static final String CACHE_KEY_ROLE_PERMISSIONS = "Authorization:rolePermissions:roleId_%s";


    public List<String> getUserPermissions() {
        Set<Long> rolesIds = UserContextHolder.getUserContext().getRolesIds();
        List<String> ret = new ArrayList<>(rolesIds.size() * 32);

        rolesIds.forEach(
                roleId -> {
                    String cacheKey = String.format(CACHE_KEY_ROLE_PERMISSIONS, roleId);
                    ret.addAll(CollUtil.emptyIfNull(stringSetRedisTemplate.opsForSet().members(cacheKey)));
                }
        );

        return ret;
    }


    public void putCache(Map<Long, Set<String>> map) {
        map.keySet().forEach(
                roleId -> {
                    String cacheKey = String.format(CACHE_KEY_ROLE_PERMISSIONS, roleId);
                    String[] cacheValues = map.get(roleId).toArray(new String[]{});
                    stringSetRedisTemplate.opsForSet().add(cacheKey, cacheValues);
                }
        );
    }
}
