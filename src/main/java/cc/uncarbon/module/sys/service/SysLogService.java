package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.entity.SysLogEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.mapper.SysLogMapper;
import cc.uncarbon.module.sys.model.request.AdminListSysLogDTO;
import cc.uncarbon.module.sys.model.response.SysLogBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 后台操作日志
 * @author Uncarbon
 */
@Slf4j
@Service
public class SysLogService extends HelioBaseServiceImpl<SysLogMapper, SysLogEntity> {

    /**
     * 后台管理-分页列表
     */
    public PageResult<SysLogBO> adminList(PageParam pageParam, AdminListSysLogDTO dto) {
        Page<SysLogEntity> entityPage = this.page(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<SysLogEntity>()
                        .lambda()
                        // 用户账号
                        .like(StrUtil.isNotBlank(dto.getUsername()), SysLogEntity::getUsername, StrUtil.cleanBlank(dto.getUsername()))
                        // 操作内容
                        .like(StrUtil.isNotBlank(dto.getOperation()), SysLogEntity::getOperation, StrUtil.cleanBlank(dto.getOperation()))
                        // 状态
                        .eq(ObjectUtil.isNotNull(dto.getStatus()), SysLogEntity::getStatus, dto.getStatus())
                        // 时间区间
                        .between(ObjectUtil.isNotNull(dto.getBeginAt()) && ObjectUtil.isNotNull(dto.getEndAt()), SysLogEntity::getCreatedAt, dto.getBeginAt(), dto.getEndAt())
                        // 排序
                        .orderByDesc(SysLogEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 通用-详情
     */
    public SysLogBO getOneById(Long entityId) {
        SysLogEntity entity = this.getById(entityId);
        SysErrorEnum.INVALID_ID.assertNotNull(entity);

        return this.entity2BO(entity);
    }




    /*
    私有方法
    ------------------------------------------------------------------------------------------------
     */

    private SysLogBO entity2BO(SysLogEntity entity) {
        if (entity == null) {
            return null;
        }

        SysLogBO bo = new SysLogBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段

        return bo;
    }

    private List<SysLogBO> entityList2BOs(List<SysLogEntity> entityList) {
        // 深拷贝
        List<SysLogBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity))
        );

        return ret;
    }

    private PageResult<SysLogBO> entityPage2BOPage(Page<SysLogEntity> entityPage) {
        PageResult<SysLogBO> ret = new PageResult<>();
        BeanUtil.copyProperties(entityPage, ret);
        ret.setRecords(this.entityList2BOs(entityPage.getRecords()));

        return ret;
    }

}