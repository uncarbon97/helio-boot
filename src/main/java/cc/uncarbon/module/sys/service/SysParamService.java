package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.entity.SysParamEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysParamMapper;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysParamDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysParamDTO;
import cc.uncarbon.module.sys.model.response.SysParamBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 系统参数
 * @author Uncarbon
 */
@Slf4j
@Service
public class SysParamService extends HelioBaseServiceImpl<SysParamMapper, SysParamEntity> {

    /**
     * 后台管理-分页列表
     */
    public PageResult<SysParamBO> adminList(PageParam pageParam, AdminListSysParamDTO dto) {
        Page<SysParamEntity> entityPage = this.page(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysParamEntity>()
                        .lambda()
                        // 键名
                        .like(StrUtil.isNotBlank(dto.getKey()), SysParamEntity::getKey, StrUtil.cleanBlank(dto.getKey()))
                        // 描述
                        .like(StrUtil.isNotBlank(dto.getDescription()), SysParamEntity::getDescription, StrUtil.cleanBlank(dto.getDescription()))
                        // 排序
                        .orderByDesc(SysParamEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 通用-详情
     */
    public SysParamBO getOneById(Long entityId) {
        SysParamEntity entity = this.getById(entityId);
        SysErrorEnum.INVALID_ID.assertNotNull(entity);

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    @SysLog(value = "新增系统参数")
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysParamDTO dto) {
        this.checkExist(dto);

        dto.setId(null);
        SysParamEntity entity = new SysParamEntity();
        BeanUtil.copyProperties(dto, entity);

        this.save(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @SysLog(value = "编辑系统参数")
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateSysParamDTO dto) {
        this.checkExist(dto);

        SysParamEntity entity = new SysParamEntity();
        BeanUtil.copyProperties(dto, entity);

        this.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @SysLog(value = "删除系统参数")
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(List<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 根据键名取值
     * @param key 键名
     * @return 成功返回值, 失败返回null
     */
    public String getParamValueByKey(String key) {
        SysParamEntity sysParamEntity = this.getOne(
                new QueryWrapper<SysParamEntity>()
                        .select(" value ")
                        .lambda()
                        .eq(SysParamEntity::getKey, key)
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
     * @param key          键名
     * @param defaultValue 默认值
     * @return 成功返回值, 失败返回defaultValue
     */
    public String getParamValueByKey(String key, String defaultValue) {
        String value = this.getParamValueByKey(key);
        if (value == null) {
            return defaultValue;
        }

        return value;
    }


    /*
    私有方法
    ------------------------------------------------------------------------------------------------
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

    private PageResult<SysParamBO> entityPage2BOPage(Page<SysParamEntity> entityPage) {
        // 深拷贝
        List<SysParamBO> boRecords = new ArrayList<>(entityPage.getRecords().size());
        entityPage.getRecords().forEach(
                entity -> boRecords.add(this.entity2BO(entity))
        );

        PageResult<SysParamBO> ret = new PageResult<>();
        BeanUtil.copyProperties(entityPage, ret);
        ret.setRecords(boRecords);
        return ret;
    }

    /**
     * 检查是否已存在同名数据
     * @param dto DTO
     */
    private void checkExist(AdminInsertOrUpdateSysParamDTO dto) {
        SysParamEntity existEntity = this.getOne(
                new QueryWrapper<SysParamEntity>()
                        .select( " id ")
                        .lambda()
                        .eq(SysParamEntity::getDescription, dto.getDescription())
                        .or()
                        .eq(SysParamEntity::getKey, dto.getKey())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existEntity != null && !existEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同系统参数，请重新输入");
        }
    }
}