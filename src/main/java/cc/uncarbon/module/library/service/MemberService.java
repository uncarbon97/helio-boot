package cc.uncarbon.module.library.service;

import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.library.entity.MemberEntity;
import cc.uncarbon.module.library.mapper.MemberMapper;
import cc.uncarbon.module.library.model.request.AdminInsertOrUpdateMemberDTO;
import cc.uncarbon.module.library.model.request.AdminListMemberDTO;
import cc.uncarbon.module.library.model.response.MemberBO;
import cc.uncarbon.module.sys.enums.GenericStatusEnum;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * 会员
 *
 * @author Uncarbon
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService extends HelioBaseServiceImpl<MemberMapper, MemberEntity> {

    /**
     * 后台管理-分页列表
     */
    public PageResult<MemberBO> adminList(PageParam pageParam, AdminListMemberDTO dto) {
        Page<MemberEntity> entityPage = this.page(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<MemberEntity>()
                        .lambda()
                        // 学号/工号
                        .like(StrUtil.isNotBlank(dto.getUsername()), MemberEntity::getUsername, StrUtil.cleanBlank(dto.getUsername()))
                        // 真实姓名
                        .like(StrUtil.isNotBlank(dto.getRealName()), MemberEntity::getRealName, StrUtil.cleanBlank(dto.getRealName()))
                        // 状态
                        .eq(ObjectUtil.isNotNull(dto.getStatus()), MemberEntity::getStatus, dto.getStatus())
                        // 性别
                        .eq(ObjectUtil.isNotNull(dto.getGender()), MemberEntity::getGender, dto.getGender())
                        // 手机号
                        .like(StrUtil.isNotBlank(dto.getPhoneNo()), MemberEntity::getPhoneNo, StrUtil.cleanBlank(dto.getPhoneNo()))
                        // 时间区间
                        .between(ObjectUtil.isNotNull(dto.getBeginAt()) && ObjectUtil.isNotNull(dto.getEndAt()), MemberEntity::getCreatedAt, dto.getBeginAt(), dto.getEndAt())
                        // 排序
                        .orderByDesc(MemberEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public MemberBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public MemberBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        MemberEntity entity = this.getById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateMemberDTO dto) {
        log.info("[后台管理-新增会员] >> DTO={}", dto);
        this.checkExistence(dto);

        dto.setId(null);
        MemberEntity entity = new MemberEntity();
        BeanUtil.copyProperties(dto, entity);

        this.save(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateMemberDTO dto) {
        log.info("[后台管理-编辑会员] >> DTO={}", dto);
        this.checkExistence(dto);

        MemberEntity entity = new MemberEntity();
        BeanUtil.copyProperties(dto, entity);

        this.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除会员] >> ids={}", ids);
        this.removeByIds(ids);
    }

    /**
     * 后台管理-列表-下拉框专用
     */
    public List<MemberBO> adminListOptions() {
        List<MemberEntity> entityList = this.list(
                new QueryWrapper<MemberEntity>()
                        .lambda()
                        // 只 SELECT 特定字段
                        .select(MemberEntity::getId, MemberEntity::getUsername, MemberEntity::getRealName)
                        // 仅启用状态
                        .eq(MemberEntity::getStatus, GenericStatusEnum.ENABLED)
        );

        return this.entityList2BOs(entityList);
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
    private MemberBO entity2BO(MemberEntity entity) {
        if (entity == null) {
            return null;
        }

        MemberBO bo = new MemberBO();
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
    private List<MemberBO> entityList2BOs(List<MemberEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<MemberBO> ret = new ArrayList<>(entityList.size());
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
    private PageResult<MemberBO> entityPage2BOPage(Page<MemberEntity> entityPage) {
        PageResult<MemberBO> ret = new PageResult<>();
        BeanUtil.copyProperties(entityPage, ret);
        ret.setRecords(this.entityList2BOs(entityPage.getRecords()));

        return ret;
    }

    /**
     * 检查是否已存在同名数据
     *
     * @param dto DTO
     */
    private void checkExistence(@NonNull AdminInsertOrUpdateMemberDTO dto) {
        /*
        可以根据自己业务需要，解禁这段代码，修改判断条件和文案

        MemberEntity existingEntity = this.getOne(
                new QueryWrapper<MemberEntity>()
                        .lambda()
                        .select(MemberEntity::getId)
                        .eq(MemberEntity::getTitle, dto.getTitle())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同会员，请重新输入");
        }
        */
    }

}
