package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.entity.SysUserRoleRelationEntity;
import cc.uncarbon.module.sys.mapper.SysUserRoleRelationMapper;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 后台用户-角色关联
 * @author Uncarbon
 */
@Slf4j
@Service
public class SysUserRoleRelationService extends HelioBaseServiceImpl<SysUserRoleRelationMapper, SysUserRoleRelationEntity> {

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
     * 取拥有角色Ids
     * @param userId 用户ID
     * @return 失败返回空列表
     */
    public Set<Long> listRoleIdsByUserId(Long userId) throws IllegalArgumentException {
        if (userId == null) {
            throw new IllegalArgumentException("userId不能为空");
        }

        return this.list(
                new QueryWrapper<SysUserRoleRelationEntity>()
                        .lambda()
                        .select(SysUserRoleRelationEntity::getRoleId)
                        .eq(SysUserRoleRelationEntity::getUserId, userId)
        ).stream().map(SysUserRoleRelationEntity::getRoleId).collect(Collectors.toSet());
    }
}
