package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.entity.SysUserDeptRelationEntity;
import cc.uncarbon.module.sys.mapper.SysUserDeptRelationMapper;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * 后台用户-部门关联
 * @author Uncarbon
 */
@Slf4j
@Service
public class SysUserDeptRelationService extends HelioBaseServiceImpl<SysUserDeptRelationMapper, SysUserDeptRelationEntity> {

    /**
     * 先清理用户ID所有关联关系, 再绑定用户ID与部门ID
     */
    public void cleanAndBind(Long userId, Long deptId) {
        this.remove(
                new QueryWrapper<SysUserDeptRelationEntity>()
                        .lambda()
                        .eq(SysUserDeptRelationEntity::getUserId, userId)
        );

        if (ObjectUtil.isNotNull(deptId)) {
            // 需要绑定部门
            this.save(
                    SysUserDeptRelationEntity.builder()
                            .userId(userId)
                            .deptId(deptId)
                            .build()
            );
        }

    }

}
