package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.entity.SysDeptEntity;
import cc.uncarbon.module.sys.entity.SysUserDeptRelationEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysDeptMapper;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysDeptDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysDeptDTO;
import cc.uncarbon.module.sys.model.response.SysDeptBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 部门
 *
 * @author Uncarbon
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysDeptService extends HelioBaseServiceImpl<SysDeptMapper, SysDeptEntity> {

    private final SysUserDeptRelationService sysUserDeptRelationService;


    /**
     * 后台管理-列表
     */
    public List<SysDeptBO> adminList(AdminListSysDeptDTO dto) {
        List<SysDeptEntity> entityList = this.list(
                new QueryWrapper<SysDeptEntity>()
                        .lambda()
                        // 名称
                        .like(StrUtil.isNotBlank(dto.getTitle()), SysDeptEntity::getTitle,
                                StrUtil.cleanBlank(dto.getTitle()))
                        // 上级ID
                        .eq(ObjectUtil.isNotNull(dto.getParentId()), SysDeptEntity::getParentId, dto.getParentId())
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
        SysDeptEntity entity = this.getById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    @SysLog(value = "新增部门")
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysDeptDTO dto) {
        log.info("[后台管理-新增部门] >> 入参={}", dto);
        this.checkExistence(dto);

        if (ObjectUtil.isNull(dto.getParentId())) {
            dto.setParentId(0L);
        }

        dto.setId(null);
        SysDeptEntity entity = new SysDeptEntity();
        BeanUtil.copyProperties(dto, entity);

        this.save(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @SysLog(value = "编辑部门")
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateSysDeptDTO dto) {
        log.info("[后台管理-编辑部门] >> 入参={}", dto);
        this.checkExistence(dto);

        if (ObjectUtil.isNull(dto.getParentId())) {
            dto.setParentId(0L);
        }

        SysDeptEntity entity = new SysDeptEntity();
        BeanUtil.copyProperties(dto, entity);

        this.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @SysLog(value = "删除部门")
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除部门] >> 入参={}", ids);
        this.removeByIds(ids);
    }

    /**
     * 取所属部门简易信息
     *
     * @param userId 用户ID
     */
    public SysDeptBO getPlainDeptByUserId(Long userId) {
        SysUserDeptRelationEntity relationEntity = sysUserDeptRelationService.getOne(
                new QueryWrapper<SysUserDeptRelationEntity>()
                        .lambda()
                        .eq(SysUserDeptRelationEntity::getUserId, userId)
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (relationEntity == null) {
            return null;
        }

        SysDeptEntity entity = this.getById(relationEntity.getDeptId());
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
                entity -> {
                    SysDeptBO bo = this.entity2BO(entity);
                    ret.add(this.traverseChildren(bo));
                }
        );

        return ret;
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminInsertOrUpdateSysDeptDTO dto) {
        SysDeptEntity existingEntity = this.getOne(
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
     * 递归遍历子级
     *
     * @param bo BO
     * @return SysDeptBO
     */
    private SysDeptBO traverseChildren(SysDeptBO bo) {
        List<SysDeptBO> children = this.adminList(
                AdminListSysDeptDTO.builder()
                        .parentId(bo.getId())
                        .build()
        );
        if (CollUtil.isEmpty(children)) {
            children = null;
        }

        bo.setChildren(children);

        return bo;
    }
}
