package cc.uncarbon.helper;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionCacheHelper {

    private final RedisTemplate<String, String> stringSetRedisTemplate;

    private static final String CACHE_KEY_ROLE_PERMISSIONS = "Authorization:permissions:roleId_%s";


    public Set<String> getPermissionCaches(Long roleId) {
        String cacheKey = String.format(CACHE_KEY_ROLE_PERMISSIONS, roleId);
        return stringSetRedisTemplate.opsForSet().members(cacheKey);
    }

    public void pubIfAbsent() {

    }
}
