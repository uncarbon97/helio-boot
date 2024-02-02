package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.module.sys.entity.SysParamEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysParamMapper;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysParamDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysParamDTO;
import cc.uncarbon.module.sys.model.response.SysParamBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * 系统参数
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysParamService {

    private final SysParamMapper sysParamMapper;


    /**
     * 后台管理-分页列表
     */
    public PageResult<SysParamBO> adminList(PageParam pageParam, AdminListSysParamDTO dto) {
        Page<SysParamEntity> entityPage = sysParamMapper.selectPage(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysParamEntity>()
                        .lambda()
                        // 键名
                        .like(CharSequenceUtil.isNotBlank(dto.getName()), SysParamEntity::getName, CharSequenceUtil.cleanBlank(dto.getName()))
                        // 描述
                        .like(CharSequenceUtil.isNotBlank(dto.getDescription()), SysParamEntity::getDescription, CharSequenceUtil.cleanBlank(dto.getDescription()))
                        // 排序
                        .orderByDesc(SysParamEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public SysParamBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public SysParamBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        SysParamEntity entity = sysParamMapper.selectById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysParamDTO dto) {
        log.info("[后台管理-新增系统参数] >> 入参={}", dto);
        this.checkExistence(dto);

        dto.setId(null);
        SysParamEntity entity = new SysParamEntity();
        BeanUtil.copyProperties(dto, entity);

        sysParamMapper.insert(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateSysParamDTO dto) {
        log.info("[后台管理-编辑系统参数] >> 入参={}", dto);
        this.checkExistence(dto);

        SysParamEntity entity = new SysParamEntity();
        BeanUtil.copyProperties(dto, entity);

        sysParamMapper.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除系统参数] >> 入参={}", ids);
        sysParamMapper.deleteBatchIds(ids);
    }

    /**
     * 根据键名取值
     *
     * @param name 键名
     * @return 成功返回键值，失败返回 null
     */
    public String getParamValueByName(String name) {
        SysParamEntity sysParamEntity = sysParamMapper.selectOne(
                new QueryWrapper<SysParamEntity>()
                        .lambda()
                        .select(SysParamEntity::getValue)
                        .eq(SysParamEntity::getName, name)
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );
        if (sysParamEntity == null) {
            return null;
        }

        return sysParamEntity.getValue();
    }

    /**
     * 根据键名取值
     *
     * @param name         键名
     * @param defaultValue 默认值
     * @return 成功返回键值，失败返回 defaultValue
     */
    public String getParamValueByName(String name, String defaultValue) {
        String value = this.getParamValueByName(name);
        if (value == null) {
            return defaultValue;
        }

        return value;
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
    private SysParamBO entity2BO(SysParamEntity entity) {
        if (entity == null) {
            return null;
        }

        SysParamBO bo = new SysParamBO();
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
    private List<SysParamBO> entityList2BOs(List<SysParamEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<SysParamBO> ret = new ArrayList<>(entityList.size());
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
    private PageResult<SysParamBO> entityPage2BOPage(Page<SysParamEntity> entityPage) {
        return new PageResult<SysParamBO>()
                .setCurrent(entityPage.getCurrent())
                .setSize(entityPage.getSize())
                .setTotal(entityPage.getTotal())
                .setRecords(this.entityList2BOs(entityPage.getRecords()));
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminInsertOrUpdateSysParamDTO dto) {
        SysParamEntity existingEntity = sysParamMapper.selectOne(
                new QueryWrapper<SysParamEntity>()
                        .lambda()
                        // 仅取主键ID
                        .select(SysParamEntity::getId)
                        // 描述相同
                        .eq(SysParamEntity::getDescription, dto.getDescription())
                        .or()
                        // 或键名相同
                        .eq(SysParamEntity::getName, dto.getName())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同系统参数，请重新输入");
        }
    }
}
