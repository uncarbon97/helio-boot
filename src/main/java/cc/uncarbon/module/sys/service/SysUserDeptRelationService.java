package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.module.sys.entity.SysUserDeptRelationEntity;
import cc.uncarbon.module.sys.mapper.SysUserDeptRelationMapper;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 后台用户-部门关联
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysUserDeptRelationService {

    private final SysUserDeptRelationMapper sysUserDeptRelationMapper;


    /**
     * 列举用户ID关联的部门IDs
     *
     * @return 关联的部门IDs；目前最多只有1个元素
     */
    public List<Long> getUserDeptIds(Long userId) {
        SysUserDeptRelationEntity entity = sysUserDeptRelationMapper.selectOne(
                new QueryWrapper<SysUserDeptRelationEntity>()
                        .lambda()
                        .eq(SysUserDeptRelationEntity::getUserId, userId)
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (entity == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(entity.getDeptId());
    }

    /**
     * 先清理用户ID所有关联关系, 再绑定用户ID与部门ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void cleanAndBind(Long userId, Long deptId) {
        sysUserDeptRelationMapper.delete(
                new QueryWrapper<SysUserDeptRelationEntity>()
                        .lambda()
                        .eq(SysUserDeptRelationEntity::getUserId, userId)
        );

        if (ObjectUtil.isNotNull(deptId)) {
            // 需要绑定部门
            sysUserDeptRelationMapper.insert(
                    SysUserDeptRelationEntity.builder()
                            .userId(userId)
                            .deptId(deptId)
                            .build()
            );
        }

    }

    /**
     * 列举部门IDs关联的用户IDs
     */
    public Set<Long> listUserIdsByDeptIds(Collection<Long> deptIds) {
        if (CollUtil.isEmpty(deptIds)) {
            return Collections.emptySet();
        }

        return sysUserDeptRelationMapper.selectList(
                new QueryWrapper<SysUserDeptRelationEntity>()
                        .lambda()
                        // 只要用户ID
                        .select(SysUserDeptRelationEntity::getUserId)
                        .in(SysUserDeptRelationEntity::getDeptId, deptIds)
        ).stream().map(SysUserDeptRelationEntity::getUserId).collect(Collectors.toSet());
    }

}
