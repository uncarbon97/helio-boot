package cc.uncarbon.module.sys.model.interior;

import cc.uncarbon.module.sys.entity.SysDeptEntity;
import cn.hutool.core.collection.CollUtil;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户关联部门容器
 */
@Getter
public class UserDeptContainer {

    /**
     * 直接关联的部门IDs
     * 一般只有0或1个元素
     */
    private final List<Long> relatedDeptIds;

    /**
     * 直接关联的部门实例集合
     * 一般只有0或1个元素
     */
    private final List<SysDeptEntity> relatedDepts;

    /**
     * 可见的部门IDs
     */
    private List<Long> visibleDeptIds;

    /**
     * 可见的部门实例集合
     */
    private List<SysDeptEntity> visibleDepts;


    public UserDeptContainer(List<Long> relatedDeptIds, List<SysDeptEntity> relatedDepts) {
        this.relatedDeptIds = relatedDeptIds;
        this.relatedDepts = relatedDepts;
        this.visibleDeptIds = relatedDeptIds;
        this.visibleDepts = relatedDepts;
    }

    /**
     * 用户是否有实际关联的部门
     */
    public boolean hasRelatedDepts() {
        return CollUtil.isNotEmpty(relatedDeptIds) && CollUtil.isNotEmpty(relatedDepts);
    }

    /**
     * 用户主要关联的部门，默认取第一个元素
     * @return null or 部门实例
     */
    public SysDeptEntity primaryRelatedDept() {
        return CollUtil.getFirst(relatedDepts);
    }

    /**
     * 用户是否有实际可见的部门
     * 也可视为部门的数据权限范围
     */
    public boolean hasVisibleDepts() {
        return CollUtil.isNotEmpty(visibleDeptIds) && CollUtil.isNotEmpty(visibleDepts);
    }

    /**
     * 更新可见的部门
     */
    public void updateVisibleDepts(List<SysDeptEntity> visibleDepts) {
        if (CollUtil.isEmpty(visibleDepts)) {
            this.visibleDeptIds = Collections.emptyList();
            this.visibleDepts = Collections.emptyList();
        } else {
            this.visibleDeptIds = visibleDepts.stream().map(SysDeptEntity::getId).collect(Collectors.toList());
            this.visibleDepts = visibleDepts;
        }
    }
}
