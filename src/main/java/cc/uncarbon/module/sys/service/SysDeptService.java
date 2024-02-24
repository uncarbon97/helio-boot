package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.entity.SysDeptEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysDeptMapper;
import cc.uncarbon.module.sys.model.interior.UserDeptContainer;
import cc.uncarbon.module.sys.model.interior.UserRoleContainer;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysDeptDTO;
import cc.uncarbon.module.sys.model.response.SysDeptBO;
import cn.hutool.core.bean.BeanUtil;
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
 * 部门
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysDeptService {

    private final SysDeptMapper sysDeptMapper;
    private final SysUserDeptRelationService sysUserDeptRelationService;
    private final SysRoleService sysRoleService;


    /**
     * 后台管理-列表
     */
    public List<SysDeptBO> adminList() {
        return entityList2BOs(sysDeptMapper.sortedList());
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public SysDeptBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id               主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public SysDeptBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        SysDeptEntity entity = sysDeptMapper.selectById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysDeptDTO dto) {
        log.info("[后台管理-新增部门] >> 入参={}", dto);
        this.checkExistence(dto);

        if (ObjectUtil.isNull(dto.getParentId())) {
            dto.setParentId(SysConstant.ROOT_PARENT_ID);
        }

        dto.setId(null);
        SysDeptEntity entity = new SysDeptEntity();
        BeanUtil.copyProperties(dto, entity);

        sysDeptMapper.insert(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateSysDeptDTO dto) {
        log.info("[后台管理-编辑部门] >> 入参={}", dto);
        this.checkExistence(dto);

        if (ObjectUtil.isNull(dto.getParentId())) {
            dto.setParentId(SysConstant.ROOT_PARENT_ID);
        }

        SysDeptEntity entity = new SysDeptEntity();
        BeanUtil.copyProperties(dto, entity);

        sysDeptMapper.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除部门] >> 入参={}", ids);
        sysDeptMapper.deleteBatchIds(ids);
    }

    /**
     * 后台管理-下拉框数据
     * @param inferiorsOnly 只能看到本部门及以下
     */
    public List<SysDeptBO> adminSelectOptions(boolean inferiorsOnly) {
        if (inferiorsOnly) {
            UserRoleContainer currentUser = sysRoleService.getCurrentUserRoleContainer();
            if (currentUser.isNotAnyAdmin()) {
                // 非管理员才会限制，只能看到本部门及以下
                UserDeptContainer deptContainer = getCurrentUserDeptContainer(true);
                return entityList2BOs(deptContainer.getVisibleDepts());
            }
        }
        // 能看所有
        return adminList();
    }

    /**
     * 取当前用户关联部门信息
     * 仅内部使用
     * @param queryVisibleDept 是否要进一步查询可见部门
     */
    protected UserDeptContainer getCurrentUserDeptContainer(boolean queryVisibleDept) {
        return getSpecifiedUserDeptContainer(UserContextHolder.getUserId(), queryVisibleDept);
    }

    /**
     * 取指定用户关联部门信息
     * 仅内部使用
     * @param queryVisibleDept 是否要进一步查询可见部门
     */
    protected UserDeptContainer getSpecifiedUserDeptContainer(Long specifiedUserId, boolean queryVisibleDept) {
        List<Long> currentUserDeptIds = sysUserDeptRelationService.getUserDeptIds(specifiedUserId);
        List<SysDeptEntity> currentUserDepts = Collections.emptyList();
        if (CollUtil.isNotEmpty(currentUserDeptIds)) {
            currentUserDepts = sysDeptMapper.selectBatchIds(currentUserDeptIds);
        }
        UserDeptContainer container = new UserDeptContainer(currentUserDeptIds, currentUserDepts);

        if (queryVisibleDept && container.hasRelatedDepts()) {
            List<SysDeptEntity> allDepts = sysDeptMapper.sortedList();
            List<SysDeptEntity> inferiors = determineAllInferiors(allDepts, container.primaryRelatedDept());
            container.updateVisibleDepts(inferiors);
        }
        return container;
    }

    /*
    ----------------------------------------------------------------
                        私有方法 private methods
    ----------------------------------------------------------------
     */

    /**
     * 实体转 BO
     *
     * @param entity 实体
     * @return BO
     */
    private SysDeptBO entity2BO(SysDeptEntity entity) {
        if (entity == null) {
            return null;
        }

        SysDeptBO bo = new SysDeptBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        if (SysConstant.ROOT_PARENT_ID.equals(bo.getParentId())) {
            // 返回前端时，不显示 parentId = 0
            bo.setParentId(null);
        }

        return bo;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @return BO List
     */
    private List<SysDeptBO> entityList2BOs(List<SysDeptEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<SysDeptBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity))
        );

        return ret;
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminInsertOrUpdateSysDeptDTO dto) {
        SysDeptEntity existingEntity = sysDeptMapper.selectOne(
                new QueryWrapper<SysDeptEntity>()
                        .lambda()
                        // 仅取主键ID
                        .select(SysDeptEntity::getId)
                        // 名称相同
                        .eq(SysDeptEntity::getTitle, dto.getTitle())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同部门，请重新输入");
        }
    }

    /**
     * 找出本部门及所有下级部门
     *
     * @param start 本部门
     * @return 本部门 + 所有下级部门
     */
    private List<SysDeptEntity> determineAllInferiors(List<SysDeptEntity> entityList, SysDeptEntity start) {
        // 结果集合
        List<SysDeptEntity> ret = new ArrayList<>(entityList.size());
        Deque<SysDeptEntity> deque = new ArrayDeque<>();

        // 转map提高效率
        Map<Long, List<SysDeptEntity>> groupByParentId = entityList.stream().collect(Collectors.groupingBy(SysDeptEntity::getParentId));

        // 起点
        ret.add(start);
        deque.add(start);

        // 循环填充下级部门实例
        while (CollUtil.isNotEmpty(deque)) {
            SysDeptEntity parent = deque.pop();
            List<SysDeptEntity> children = groupByParentId.get(parent.getId());
            if (CollUtil.isNotEmpty(children)) {
                ret.addAll(children);
                deque.addAll(children);
            }
        }
        return ret;
    }
}
