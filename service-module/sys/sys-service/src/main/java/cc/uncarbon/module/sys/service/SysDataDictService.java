package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.module.sys.entity.SysDataDictEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysDataDictMapper;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysDataDictDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysDataDictDTO;
import cc.uncarbon.module.sys.model.response.SysDataDictBO;
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
 * 数据字典
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysDataDictService {

    private final SysDataDictMapper sysDataDictMapper;


    /**
     * 后台管理-分页列表
     */
    public PageResult<SysDataDictBO> adminList(PageParam pageParam, AdminListSysDataDictDTO dto) {
        Page<SysDataDictEntity> entityPage = sysDataDictMapper.selectPage(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysDataDictEntity>()
                        .lambda()
                        // 参数描述
                        .like(CharSequenceUtil.isNotBlank(dto.getDescription()), SysDataDictEntity::getDescription, CharSequenceUtil.cleanBlank(dto.getDescription()))
                        // 排序
                        .orderByDesc(SysDataDictEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public SysDataDictBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public SysDataDictBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        SysDataDictEntity entity = sysDataDictMapper.selectById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysDataDictDTO dto) {
        log.info("[后台管理-新增数据字典] >> 入参={}", dto);
        this.checkExistence(dto);

        dto.setId(null);
        SysDataDictEntity entity = new SysDataDictEntity();
        BeanUtil.copyProperties(dto, entity);

        sysDataDictMapper.insert(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateSysDataDictDTO dto) {
        log.info("[后台管理-编辑数据字典] >> 入参={}", dto);
        this.checkExistence(dto);

        SysDataDictEntity entity = new SysDataDictEntity();
        BeanUtil.copyProperties(dto, entity);

        sysDataDictMapper.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除数据字典] >> 入参={}", ids);
        sysDataDictMapper.deleteBatchIds(ids);
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
    private SysDataDictBO entity2BO(SysDataDictEntity entity) {
        if (entity == null) {
            return null;
        }

        SysDataDictBO bo = new SysDataDictBO();
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
    private List<SysDataDictBO> entityList2BOs(List<SysDataDictEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<SysDataDictBO> ret = new ArrayList<>(entityList.size());
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
    private PageResult<SysDataDictBO> entityPage2BOPage(Page<SysDataDictEntity> entityPage) {
        return new PageResult<SysDataDictBO>()
                .setCurrent(entityPage.getCurrent())
                .setSize(entityPage.getSize())
                .setTotal(entityPage.getTotal())
                .setRecords(this.entityList2BOs(entityPage.getRecords()))
                ;
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminInsertOrUpdateSysDataDictDTO dto) {
        SysDataDictEntity existingEntity = sysDataDictMapper.selectOne(
                new QueryWrapper<SysDataDictEntity>()
                        .lambda()
                        // 仅取主键ID
                        .select(SysDataDictEntity::getId)
                        // 驼峰式键名相同
                        .eq(SysDataDictEntity::getCamelCaseKey, dto.getCamelCaseKey())
                        .or()
                        // 或帕斯卡式键名相同
                        .eq(SysDataDictEntity::getPascalCaseKey, dto.getPascalCaseKey())
                        .or()
                        // 或下划线式键名相同
                        .eq(SysDataDictEntity::getUnderCaseKey, dto.getUnderCaseKey())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同数据字典，请重新输入");
        }
    }

}
