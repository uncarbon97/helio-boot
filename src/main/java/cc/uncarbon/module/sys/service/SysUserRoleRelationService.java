package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.entity.SysUserRoleRelationEntity;
import cc.uncarbon.module.sys.mapper.SysUserRoleRelationMapper;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 后台用户-角色关联
 * @author Uncarbon
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserRoleRelationService extends HelioBaseServiceImpl<SysUserRoleRelationMapper, SysUserRoleRelationEntity> {

    private final RedisTemplate<String, Long> longSetRedisTemplate;

    /**
     * 先清理用户ID所有关联关系, 再绑定用户ID与角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void cleanAndBind(Long userId, Collection<Long> roleIds) {
        this.remove(
                new QueryWrapper<SysUserRoleRelationEntity>()
                        .lambda()
                        .eq(SysUserRoleRelationEntity::getUserId, userId)
        );

        if (CollUtil.isNotEmpty(roleIds)) {
            // 需要绑定角色
            roleIds.forEach(
                    roleId -> this.save(
                            SysUserRoleRelationEntity.builder()
                                    .userId(userId)
                                    .roleId(roleId)
                                    .build()
                    )
            );
        }
    }

    /**
     * 根据角色Ids取用户Ids
     */
    public Set<Long> listUserIdByRoleIds(Collection<Long> roleIds) throws IllegalArgumentException {
        if (CollUtil.isEmpty(roleIds)) {
            throw new IllegalArgumentException("roleIds不能为空");
        }

        return this.list(
                new QueryWrapper<SysUserRoleRelationEntity>()
                        .lambda()
                        .select(SysUserRoleRelationEntity::getUserId)
                        .in(SysUserRoleRelationEntity::getRoleId, roleIds)
        ).stream().map(SysUserRoleRelationEntity::getUserId).collect(Collectors.toSet());
    }

    /**
     * 取拥有角色Ids
     * @param userId 用户ID
     * @return 失败返回空列表
     */
    public Set<Long> listRoleIdByUserId(Long userId) throws IllegalArgumentException {
        if (userId == null) {
            throw new IllegalArgumentException("userId不能为空");
        }

        // 尝试从缓存中读取
        String cacheKey = String.format(SysConstant.REDIS_KEY_USER_OWNED_ROLE_IDS, userId);
        Set<Long> ret = longSetRedisTemplate.opsForSet().members(cacheKey);
        if (CollUtil.isNotEmpty(ret)) {
            return ret;
        }

        ret = this.list(
                new QueryWrapper<SysUserRoleRelationEntity>()
                        .lambda()
                        .select(SysUserRoleRelationEntity::getRoleId)
                        .eq(SysUserRoleRelationEntity::getUserId, userId)
        ).stream().map(SysUserRoleRelationEntity::getRoleId).collect(Collectors.toSet());

        // 写入缓存
        Long[] arr = new Long[ret.size()];
        longSetRedisTemplate.opsForSet().add(cacheKey, ret.toArray(arr));
        longSetRedisTemplate.expire(cacheKey, SysConstant.ONE_DAY_SECONDS, TimeUnit.SECONDS);

        return ret;
    }
}
