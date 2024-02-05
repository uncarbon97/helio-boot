package cc.uncarbon.module.sys.service;

import cc.uncarbon.module.sys.entity.SysUserRoleRelationEntity;
import cc.uncarbon.module.sys.mapper.SysUserRoleRelationMapper;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 后台用户-角色关联
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysUserRoleRelationService {

    private final SysUserRoleRelationMapper sysUserRoleRelationMapper;


    /**
     * 后台管理-新增
     * 注：本方法较为特殊，仅供SysTenantFacadeImpl调用
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(Long tenantId, Long userId, Long roleId) {
        SysUserRoleRelationEntity entity = new SysUserRoleRelationEntity()
                .setUserId(userId).setRoleId(roleId);
        entity.setTenantId(tenantId);

        sysUserRoleRelationMapper.insert(entity);

        return entity.getId();
    }


    /**
     * 先清理用户ID所有关联关系, 再绑定用户ID与角色ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void cleanAndBind(Long userId, Collection<Long> roleIds) {
        sysUserRoleRelationMapper.delete(
                new QueryWrapper<SysUserRoleRelationEntity>()
                        .lambda()
                        .eq(SysUserRoleRelationEntity::getUserId, userId)
        );

        if (CollUtil.isNotEmpty(roleIds)) {
            // 需要绑定角色
            roleIds.forEach(
                    roleId -> sysUserRoleRelationMapper.insert(
                            SysUserRoleRelationEntity.builder()
                                    .userId(userId)
                                    .roleId(roleId)
                                    .build()
                    )
            );
        }
    }

    /**
     * 取拥有角色Ids
     *
     * @param userId 用户ID
     * @return 失败返回空列表
     */
    public Set<Long> listRoleIdsByUserId(Long userId) throws IllegalArgumentException {
        if (userId == null) {
            throw new IllegalArgumentException("userId不能为空");
        }

        return sysUserRoleRelationMapper.selectList(
                new QueryWrapper<SysUserRoleRelationEntity>()
                        .lambda()
                        .select(SysUserRoleRelationEntity::getRoleId)
                        .eq(SysUserRoleRelationEntity::getUserId, userId)
        ).stream().map(SysUserRoleRelationEntity::getRoleId).collect(Collectors.toSet());
    }

    /**
     * 取角色IDs关联的用户IDs
     *
     * @param roleIds 角色IDs
     * @return 空集合or用户IDs
     */
    public Set<Long> listUserIdsByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptySet();
        }

        return sysUserRoleRelationMapper.selectList(
                new QueryWrapper<SysUserRoleRelationEntity>()
                        .lambda()
                        .select(SysUserRoleRelationEntity::getUserId)
                        .in(SysUserRoleRelationEntity::getRoleId, roleIds)
        ).stream().map(SysUserRoleRelationEntity::getUserId).collect(Collectors.toSet());
    }
}
