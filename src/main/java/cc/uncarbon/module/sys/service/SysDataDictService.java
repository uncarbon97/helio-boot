package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.module.sys.entity.SysDataDictClassifiedEntity;
import cc.uncarbon.module.sys.entity.SysDataDictItemEntity;
import cc.uncarbon.module.sys.mapper.SysDataDictClassifiedMapper;
import cc.uncarbon.module.sys.mapper.SysDataDictItemMapper;
import cc.uncarbon.module.sys.model.request.AdminSysDataDictClassifiedInsertOrUpdateDTO;
import cc.uncarbon.module.sys.model.request.AdminSysDataDictClassifiedListDTO;
import cc.uncarbon.module.sys.model.request.AdminSysDataDictItemInsertOrUpdateDTO;
import cc.uncarbon.module.sys.model.request.AdminSysDataDictItemListDTO;
import cc.uncarbon.module.sys.model.response.SysDataDictClassifiedBO;
import cc.uncarbon.module.sys.model.response.SysDataDictItemBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 数据字典
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysDataDictService {

    private final SysDataDictClassifiedMapper sysDataDictClassifiedMapper;
    private final SysDataDictItemMapper sysDataDictItemMapper;


    /**
     * 后台管理-分页列表数据字典分类
     */
    public PageResult<SysDataDictClassifiedBO> adminListClassified(PageParam pageParam, AdminSysDataDictClassifiedListDTO dto) {
        Page<SysDataDictClassifiedEntity> entityPage = sysDataDictClassifiedMapper.selectPage(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysDataDictClassifiedEntity>()
                        .lambda()
                        // 分类编码
                        .eq(CharSequenceUtil.isNotBlank(dto.getCode()), SysDataDictClassifiedEntity::getCode, CharSequenceUtil.cleanBlank(dto.getCode()))
                        // 排序
                        .orderByDesc(SysDataDictClassifiedEntity::getCreatedAt)
        );

        return this.classifiedEntityPage2BOPage(entityPage);
    }

    /**
     * 后台管理-新增数据字典分类
     *
     * @return 主键ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsertClassified(AdminSysDataDictClassifiedInsertOrUpdateDTO dto) {
        log.info("[后台管理-新增数据字典分类] >> 入参={}", dto);
        this.checkExistence(dto);

        dto.setId(null);
        SysDataDictClassifiedEntity entity = new SysDataDictClassifiedEntity();
        BeanUtil.copyProperties(dto, entity);

        sysDataDictClassifiedMapper.insert(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑数据字典分类
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdateClassified(AdminSysDataDictClassifiedInsertOrUpdateDTO dto) {
        log.info("[后台管理-编辑数据字典分类] >> 入参={}", dto);
        this.checkExistence(dto);

        SysDataDictClassifiedEntity entity = new SysDataDictClassifiedEntity();
        BeanUtil.copyProperties(dto, entity);

        sysDataDictClassifiedMapper.updateById(entity);
    }

    /**
     * 后台管理-删除数据字典分类
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDeleteClassified(Collection<Long> ids) {
        log.info("[后台管理-删除数据字典分类] >> 入参={}", ids);
        sysDataDictClassifiedMapper.deleteBatchIds(ids);
    }

    /**
     * 后台管理-分页列表数据字典分类下的字典项
     */
    public PageResult<SysDataDictItemBO> adminListItem(PageParam pageParam, AdminSysDataDictItemListDTO dto) {
        Page<SysDataDictItemEntity> entityPage = sysDataDictItemMapper.selectPage(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysDataDictItemEntity>()
                        .lambda()
                        // 分类ID
                        .eq(SysDataDictItemEntity::getClassifiedId, dto.getClassifiedId())
                        // 排序
                        .orderByAsc(SysDataDictItemEntity::getSort)
        );

        return this.itemEntityPage2BOPage(entityPage);
    }

    /**
     * 后台管理-新增数据字典项
     *
     * @return 主键ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsertItem(AdminSysDataDictItemInsertOrUpdateDTO dto) {
        log.info("[后台管理-新增数据字典项] >> 入参={}", dto);
        this.checkExistence(dto);

        dto.setId(null);
        SysDataDictItemEntity entity = new SysDataDictItemEntity();
        BeanUtil.copyProperties(dto, entity);

        sysDataDictItemMapper.insert(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑数据字典项
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdateItem(AdminSysDataDictItemInsertOrUpdateDTO dto) {
        log.info("[后台管理-编辑数据字典项] >> 入参={}", dto);
        this.checkExistence(dto);

        SysDataDictItemEntity entity = new SysDataDictItemEntity();
        BeanUtil.copyProperties(dto, entity);

        sysDataDictItemMapper.updateById(entity);
    }

    /**
     * 后台管理-删除数据字典项
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDeleteItem(Collection<Long> ids, Long classifiedId) {
        log.info("[后台管理-删除数据字典项] >> 分类ID={}  入参={}", classifiedId, ids);
        sysDataDictItemMapper.delete(
                new QueryWrapper<SysDataDictItemEntity>()
                        .lambda()
                        .eq(SysDataDictItemEntity::getClassifiedId, classifiedId)
                        .in(SysDataDictItemEntity::getId, ids)
        );
    }

    /**
     * 列举指定分类编码下的所有启用的字典项
     *
     * @return 存在则返回字典项列表；不存在或没有符合的字典项，均返回空列表
     */
    public List<SysDataDictItemBO> listEnabledItemsByClassifiedCode(@Nonnull String classifiedCode) {
        SysDataDictClassifiedEntity classified =
                sysDataDictClassifiedMapper.selectByCode(classifiedCode, EnabledStatusEnum.ENABLED);
        if (Objects.isNull(classified)) {
            return Collections.emptyList();
        }
        return itemEntityList2BOs(
                sysDataDictItemMapper.selectList(
                        new QueryWrapper<SysDataDictItemEntity>()
                                .lambda()
                                // 分类ID
                                .eq(SysDataDictItemEntity::getClassifiedId, classified.getId())
                                // 状态
                                .eq(SysDataDictItemEntity::getStatus, EnabledStatusEnum.ENABLED)
                                // 排序
                                .orderByAsc(SysDataDictItemEntity::getSort)
                )
        );
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
    private SysDataDictClassifiedBO entity2BO(SysDataDictClassifiedEntity entity) {
        if (entity == null) {
            return null;
        }

        SysDataDictClassifiedBO bo = new SysDataDictClassifiedBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段

        return bo;
    }

    /**
     * 实体转 BO
     *
     * @param entity 实体
     * @return BO
     */
    private SysDataDictItemBO entity2BO(SysDataDictItemEntity entity) {
        if (entity == null) {
            return null;
        }

        SysDataDictItemBO bo = new SysDataDictItemBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段

        return bo;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @return BO List
     */
    private List<SysDataDictClassifiedBO> classifiedEntityList2BOs(List<SysDataDictClassifiedEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<SysDataDictClassifiedBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity))
        );

        return ret;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @return BO List
     */
    private List<SysDataDictItemBO> itemEntityList2BOs(List<SysDataDictItemEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<SysDataDictItemBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity))
        );

        return ret;
    }

    /**
     * 实体分页转 BO 分页
     *
     * @param entityPage 实体分页
     * @return BO 分页
     */
    private PageResult<SysDataDictClassifiedBO> classifiedEntityPage2BOPage(Page<SysDataDictClassifiedEntity> entityPage) {
        return new PageResult<SysDataDictClassifiedBO>()
                .setCurrent(entityPage.getCurrent())
                .setSize(entityPage.getSize())
                .setTotal(entityPage.getTotal())
                .setRecords(this.classifiedEntityList2BOs(entityPage.getRecords()));
    }

    /**
     * 实体分页转 BO 分页
     *
     * @param entityPage 实体分页
     * @return BO 分页
     */
    private PageResult<SysDataDictItemBO> itemEntityPage2BOPage(Page<SysDataDictItemEntity> entityPage) {
        return new PageResult<SysDataDictItemBO>()
                .setCurrent(entityPage.getCurrent())
                .setSize(entityPage.getSize())
                .setTotal(entityPage.getTotal())
                .setRecords(this.itemEntityList2BOs(entityPage.getRecords()));
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminSysDataDictClassifiedInsertOrUpdateDTO dto) {
        SysDataDictClassifiedEntity existingEntity = sysDataDictClassifiedMapper.selectOne(
                new QueryWrapper<SysDataDictClassifiedEntity>()
                        .lambda()
                        // 仅取主键ID
                        .select(SysDataDictClassifiedEntity::getId)
                        // 分类编码相同
                        .eq(SysDataDictClassifiedEntity::getCode, dto.getCode())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同数据字典分类，请重新输入");
        }
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminSysDataDictItemInsertOrUpdateDTO dto) {
        SysDataDictItemEntity existingEntity = sysDataDictItemMapper.selectOne(
                new QueryWrapper<SysDataDictItemEntity>()
                        .lambda()
                        // 仅取主键ID
                        .select(SysDataDictItemEntity::getId)
                        // 分类ID相同
                        .eq(SysDataDictItemEntity::getClassifiedId, dto.getClassifiedId())
                        // 分类编码相同
                        .eq(SysDataDictItemEntity::getCode, dto.getCode())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同数据字典项，请重新输入");
        }
    }

}
