package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.entity.SysDeptEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysDeptMapper;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * 部门
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysDeptService {

    private final SysDeptMapper sysDeptMapper;
    private final SysUserDeptRelationService sysUserDeptRelationService;


    /**
     * 后台管理-列表
     */
    public List<SysDeptBO> adminList() {
        List<SysDeptEntity> entityList = sysDeptMapper.selectList(
                new QueryWrapper<SysDeptEntity>()
                        .lambda()
                        // 排序
                        .orderByAsc(SysDeptEntity::getSort)
        );

        return this.entityList2BOs(entityList);
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
     * @param id 主键ID
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
     * 取所属部门简易信息
     *
     * @param userId 用户ID
     */
    public SysDeptBO getPlainDeptByUserId(Long userId) {
        List<Long> deptIds = sysUserDeptRelationService.getUserDeptIds(userId);
        if (CollUtil.isEmpty(deptIds)) {
            return null;
        }
        SysDeptEntity entity = sysDeptMapper.selectById(CollUtil.getFirst(deptIds));
        return this.entity2BO(entity);
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
}
