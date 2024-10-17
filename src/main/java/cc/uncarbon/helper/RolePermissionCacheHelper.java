package cc.uncarbon.helper;

import cc.uncarbon.framework.core.context.UserContextHolder;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 将角色对应权限，缓存至 Redis
 * 参考文章: https://sa-token.cc/doc.html#/fun/jur-cache
 *
 * @author Uncarbon
 */
@Component
@RequiredArgsConstructor
public class RolePermissionCacheHelper {

    private final RedisTemplate<String, Collection<String>> stringSetRedisTemplate;

    private static final String CACHE_KEY_ROLE_PERMISSIONS = "Authorization:rolePermissions:roleId_%s";


    /**
     * 从缓存中取得当前用户拥有的所有权限名集合
     *
     * @return List<String>
     */
    public List<String> getUserPermissions() {
        Set<Long> rolesIds = UserContextHolder.getUserContext().getRolesIds();
        if (CollUtil.isEmpty(rolesIds)) {
            return Collections.emptyList();
        }

        // 批量查询缓存
        List<String> cacheKeys = rolesIds.stream()
                .map(roleId -> String.format(CACHE_KEY_ROLE_PERMISSIONS, roleId))
                .toList();
        List<Collection<String>> cacheValues = stringSetRedisTemplate.opsForValue().multiGet(cacheKeys);
        if (CollUtil.isEmpty(cacheValues)) {
            return Collections.emptyList();
        }

        return cacheValues.stream().flatMap(Collection::stream).toList();
    }

    /**
     * 覆盖更新角色对应权限至 Redis
     *
     * @param map key=角色ID value=权限集合
     */
    public void putCache(Map<Long, Set<String>> map) {
        Set<Map.Entry<Long, Set<String>>> entries = map.entrySet();
        entries.forEach(
                entry -> this.putCache(entry.getKey(), entry.getValue())
        );
    }

    /**
     * 覆盖更新角色对应权限至 Redis
     *
     * @param roleId 角色ID
     * @param newPermissions 新权限名集合
     */
    public void putCache(Long roleId, Collection<String> newPermissions) {
        String cacheKey = String.format(CACHE_KEY_ROLE_PERMISSIONS, roleId);
        stringSetRedisTemplate.opsForValue().set(cacheKey, newPermissions);
    }

    /**
     * 删除角色ID对应的权限缓存
     *
     * @param roleIds 角色ID集合
     */
    public void deleteCache(Collection<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            // 批量删除缓存
            List<String> cacheKeys = roleIds.stream().map(roleId -> String.format(CACHE_KEY_ROLE_PERMISSIONS, roleId)).toList();
            stringSetRedisTemplate.delete(cacheKeys);
        }
    }
}
