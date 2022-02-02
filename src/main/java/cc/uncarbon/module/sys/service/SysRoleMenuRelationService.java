package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.entity.SysRoleMenuRelationEntity;
import cc.uncarbon.module.sys.mapper.SysRoleMenuRelationMapper;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 后台角色-可见菜单关联
 *
 * @author Uncarbon
 */
@Slf4j
@Service
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

        return this.list(
                new QueryWrapper<SysRoleMenuRelationEntity>()
                        .lambda()
                        .select(SysRoleMenuRelationEntity::getMenuId)
                        .in(SysRoleMenuRelationEntity::getRoleId, roleIds)
        ).stream().map(SysRoleMenuRelationEntity::getMenuId).collect(Collectors.toSet());
    }

    /**
     * 先清理角色ID所有关联关系, 再绑定角色ID与菜单ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void cleanAndBind(Long roleId, List<Long> menuIds) {
        this.remove(
                new QueryWrapper<SysRoleMenuRelationEntity>()
                        .lambda()
                        .eq(SysRoleMenuRelationEntity::getRoleId, roleId)
        );

        if (CollUtil.isNotEmpty(menuIds)) {
            // 需要绑定菜单

            menuIds.forEach(
                    menuId -> this.save(
                            SysRoleMenuRelationEntity.builder()
                                    .roleId(roleId)
                                    .menuId(menuId)
                                    .build()
                    )
            );
        }
    }
}
