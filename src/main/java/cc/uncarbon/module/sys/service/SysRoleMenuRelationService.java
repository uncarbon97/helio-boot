package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.entity.SysRoleMenuRelationEntity;
import cc.uncarbon.module.sys.mapper.SysRoleMenuRelationMapper;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 后台角色-可见菜单关联
 *
 * @author Uncarbon
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleMenuRelationService extends HelioBaseServiceImpl<SysRoleMenuRelationMapper, SysRoleMenuRelationEntity> {

    private final RedisTemplate<String, Set<Long>> longSetRedisTemplate;


    /**
     * 根据角色Ids取菜单Ids
     * 因为多种角色容易出现交集，所以干脆用 Set
     *
     * @param roleIds 角色Ids
     * @return 菜单Ids
     */
    public Set<Long> listMenuIdByRoleIds(Collection<Long> roleIds) throws IllegalArgumentException {
        if (CollUtil.isEmpty(roleIds)) {
            throw new IllegalArgumentException("roleIds不能为空");
        }

        Set<Long> ret = new HashSet<>(roleIds.size() * 16);
        roleIds.forEach(
                roleId -> {
                    String cacheKey = "listMenuIdByRoleIds_" + roleId;
                    Set<Long> cacheValue = longSetRedisTemplate.opsForValue().get(cacheKey);

                    if (CollUtil.isEmpty(cacheValue)) {
                        // 为空则查库
                        cacheValue = this.list(
                                new QueryWrapper<SysRoleMenuRelationEntity>()
                                        .lambda()
                                        .select(SysRoleMenuRelationEntity::getMenuId)
                                        .eq(SysRoleMenuRelationEntity::getRoleId, roleId)
                        ).stream().map(SysRoleMenuRelationEntity::getMenuId).collect(Collectors.toSet());

                        // 并写入缓存
                        longSetRedisTemplate.opsForValue().set(cacheKey, cacheValue, 1, TimeUnit.DAYS);
                    }

                    // 加入最终结果
                    ret.addAll(cacheValue);
                }
        );

        return ret;
    }

    /**
     * 先清理角色ID所有关联关系, 再绑定角色ID与菜单ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void cleanAndBind(Long roleId, Collection<Long> menuIds) {
        this.remove(
                new QueryWrapper<SysRoleMenuRelationEntity>()
                        .lambda()
                        .eq(SysRoleMenuRelationEntity::getRoleId, roleId)
        );

        if (CollUtil.isNotEmpty(menuIds)) {
            // 需要绑定菜单
            // TODO 差值新增修改 记得文档

            List<SysRoleMenuRelationEntity> entityList = new ArrayList<>(menuIds.size());
            menuIds.forEach(
                    menuId -> entityList.add(SysRoleMenuRelationEntity.builder()
                            .roleId(roleId)
                            .menuId(menuId)
                            .build()
                    )
            );

            this.saveBatch(entityList);
        }

        // TODO 清理缓存  记得文档
        String cacheKey = "listMenuIdByRoleIds_" + roleId;
        longSetRedisTemplate.delete(cacheKey);
    }
}
