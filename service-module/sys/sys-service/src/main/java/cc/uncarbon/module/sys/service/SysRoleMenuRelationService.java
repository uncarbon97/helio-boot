package cc.uncarbon.module.sys.service;

import cc.uncarbon.module.sys.entity.SysRoleMenuRelationEntity;
import cc.uncarbon.module.sys.mapper.SysRoleMenuRelationMapper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 后台角色-可见菜单关联
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysRoleMenuRelationService {

    private final SysRoleMenuRelationMapper sysRoleMenuRelationMapper;


    /**
     * 根据角色Ids取菜单Ids
     * 因为多种角色容易出现交集，所以干脆用 Set
     *
     * @param roleIds 角色Ids
     * @return 菜单Ids
     */
    public Set<Long> listMenuIdsByRoleIds(Collection<Long> roleIds) throws IllegalArgumentException {
        Assert.notEmpty(roleIds);

        // aka * 16
        Set<Long> ret = new HashSet<>(roleIds.size() << 4);
        for (Long roleId : roleIds) {
            ret.addAll(
                    sysRoleMenuRelationMapper.selectList(
                            new QueryWrapper<SysRoleMenuRelationEntity>()
                                    .lambda()
                                    .select(SysRoleMenuRelationEntity::getMenuId)
                                    .eq(SysRoleMenuRelationEntity::getRoleId, roleId)
                    ).stream().map(SysRoleMenuRelationEntity::getMenuId).collect(Collectors.toSet()));
        }

        return ret;
    }

    /**
     * 绑定角色ID与菜单ID关联关系，增量更新
     *
     * @param roleId  角色ID
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
            sysRoleMenuRelationMapper.delete(menuIdsQuery);
            return;
        }

        /*
        先删除不再需要的关联关系
         */
        sysRoleMenuRelationMapper.delete(
                new QueryWrapper<SysRoleMenuRelationEntity>()
                        .lambda()
                        .eq(SysRoleMenuRelationEntity::getRoleId, roleId)
                        .notIn(SysRoleMenuRelationEntity::getMenuId, menuIds)
        );

        /*
        取出需要增量更新的部分
         */
        Set<Long> existingMenuIds = sysRoleMenuRelationMapper.selectList(menuIdsQuery)
                .stream().map(SysRoleMenuRelationEntity::getMenuId)
                .collect(Collectors.toSet());
        menuIds.removeAll(existingMenuIds);

        if (CollUtil.isEmpty(menuIds)) {
            // 没有需要增量更新的部分
            return;
        }

        /*
        批量插入需要增量更新的部分
         */
        List<SysRoleMenuRelationEntity> entityList = new ArrayList<>(menuIds.size());
        for (Long menuId : menuIds) {
            entityList.add(
                    SysRoleMenuRelationEntity.builder()
                            .roleId(roleId)
                            .menuId(menuId)
                            .build()
            );
        }
        entityList.forEach(sysRoleMenuRelationMapper::insert);
    }
}
