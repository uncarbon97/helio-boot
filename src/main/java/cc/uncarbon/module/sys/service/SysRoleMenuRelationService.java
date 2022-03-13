package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.entity.SysRoleMenuRelationEntity;
import cc.uncarbon.module.sys.mapper.SysRoleMenuRelationMapper;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                roleId -> ret.addAll(this.list(
                            new QueryWrapper<SysRoleMenuRelationEntity>()
                                    .lambda()
                                    .select(SysRoleMenuRelationEntity::getMenuId)
                                    .eq(SysRoleMenuRelationEntity::getRoleId, roleId)
                    ).stream().map(SysRoleMenuRelationEntity::getMenuId).collect(Collectors.toSet()))
        );

        return ret;
    }

    /**
     * 绑定角色ID与菜单ID关联关系，增量更新
     *
     * @param roleId 角色ID
     * @param menuIds 新菜单ID集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void cleanAndBind(Long roleId, Collection<Long> menuIds) {
        LambdaQueryWrapper<SysRoleMenuRelationEntity> menuIdsQuery =
                new QueryWrapper<SysRoleMenuRelationEntity>()
                        .lambda()
                        .select(SysRoleMenuRelationEntity::getMenuId)
                        .eq(SysRoleMenuRelationEntity::getRoleId, roleId);

        if (CollUtil.isEmpty(menuIds)) {
            // 清除绑定，直接删除所有关联关系就行
            this.remove(menuIdsQuery);
            return;
        }

        /*
        先删除不再需要的关联关系
         */
        this.remove(
                new QueryWrapper<SysRoleMenuRelationEntity>()
                        .lambda()
                        .eq(SysRoleMenuRelationEntity::getRoleId, roleId)
                        .notIn(SysRoleMenuRelationEntity::getMenuId, menuIds)
        );

        /*
        取出需要增量更新的部分
         */
        Set<Long> existingMenuIds = this.list(menuIdsQuery).stream().map(SysRoleMenuRelationEntity::getMenuId)
                .collect(Collectors.toSet());

        menuIds.removeAll(existingMenuIds);

        /*
        构造 && 批量插入
         */
        if (!menuIds.isEmpty()) {
            List<SysRoleMenuRelationEntity> entityList = new ArrayList<>();
            menuIds.forEach(
                    menuId -> entityList.add(SysRoleMenuRelationEntity.builder()
                            .roleId(roleId)
                            .menuId(menuId)
                            .build()
                    )
            );

            this.saveBatch(entityList);
        }
    }

    /**
     * 根据角色Ids取菜单Ids
     * 因为多种角色容易出现交集，所以干脆用 Set
     *
     * @param roleId 角色Id
     * @return 菜单Ids
     */
    public Set<Long> listMenuIdByRoleId(Long roleId) {
        if (Objects.isNull(roleId)) {
           return Collections.emptySet();
        }

        return this.list(
                new QueryWrapper<SysRoleMenuRelationEntity>()
                        .lambda()
                        .select(SysRoleMenuRelationEntity::getMenuId)
                        .eq(SysRoleMenuRelationEntity::getRoleId, roleId)
        ).stream().map(SysRoleMenuRelationEntity::getMenuId).collect(Collectors.toSet());
    }
}
