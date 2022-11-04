package cc.uncarbon.module.library.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.library.constant.LibraryConstant;
import cc.uncarbon.module.library.entity.BookClassifiedEntity;
import cc.uncarbon.module.library.mapper.BookClassifiedMapper;
import cc.uncarbon.module.library.model.request.AdminInsertOrUpdateBookClassifiedDTO;
import cc.uncarbon.module.library.model.response.BookClassifiedBO;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 书籍类别
 *
 * @author Uncarbon
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookClassifiedService extends HelioBaseServiceImpl<BookClassifiedMapper, BookClassifiedEntity> {

    /**
     * 后台管理-列表
     */
    public List<BookClassifiedBO> adminList() {
        List<BookClassifiedEntity> entityList = this.list(
                new QueryWrapper<BookClassifiedEntity>()
                        .lambda()
                        // 排序
                        .orderByAsc(BookClassifiedEntity::getSort)
        );

        return this.entityList2BOs(entityList);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public BookClassifiedBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public BookClassifiedBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        BookClassifiedEntity entity = this.getById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateBookClassifiedDTO dto) {
        log.info("[后台管理-新增书籍类别] >> DTO={}", dto);
        this.checkExistence(dto);

        dto.setId(null);
        BookClassifiedEntity entity = new BookClassifiedEntity();
        BeanUtil.copyProperties(dto, entity);

        this.save(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateBookClassifiedDTO dto) {
        log.info("[后台管理-编辑书籍类别] >> DTO={}", dto);
        this.checkExistence(dto);

        BookClassifiedEntity entity = new BookClassifiedEntity();
        BeanUtil.copyProperties(dto, entity);

        this.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除书籍类别] >> ids={}", ids);
        this.removeByIds(ids);
    }

    /**
     * 后台管理-列表-下拉框专用
     */
    public List<BookClassifiedBO> adminListOptions() {
        List<BookClassifiedEntity> entityList = this.list(
                new QueryWrapper<BookClassifiedEntity>()
                        .lambda()
                        // 只 SELECT 特定字段
                        .select(BookClassifiedEntity::getId, BookClassifiedEntity::getTitle, BookClassifiedEntity::getParentId)
                        // 排序
                        .orderByAsc(BookClassifiedEntity::getSort)
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
    private BookClassifiedBO entity2BO(BookClassifiedEntity entity) {
        if (entity == null) {
            return null;
        }

        BookClassifiedBO bo = new BookClassifiedBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        if (LibraryConstant.ROOT_PARENT_ID.equals(bo.getParentId())) {
            // 根节点返回前端时按 null 处理
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
    private List<BookClassifiedBO> entityList2BOs(List<BookClassifiedEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<BookClassifiedBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
            entity -> ret.add(this.entity2BO(entity))
        );

        return ret;
    }

    /**
     * 检查是否已存在同名数据
     *
     * @param dto DTO
     */
    private void checkExistence(@NonNull AdminInsertOrUpdateBookClassifiedDTO dto) {
        BookClassifiedEntity existingEntity = this.getOne(
                new QueryWrapper<BookClassifiedEntity>()
                        .lambda()
                        .select(BookClassifiedEntity::getId)
                        .eq(BookClassifiedEntity::getTitle, dto.getTitle())
                        // 上级ID=0，表示为根节点
                        .eq(BookClassifiedEntity::getParentId, LibraryConstant.ROOT_PARENT_ID)
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同的最高级书籍类别，请重新输入");
        }

        existingEntity = this.getOne(
                new QueryWrapper<BookClassifiedEntity>()
                        .lambda()
                        .select(BookClassifiedEntity::getId)
                        .eq(BookClassifiedEntity::getTitle, dto.getTitle())
                        // 同一个父级类别下，有重复的标题
                        .eq(BookClassifiedEntity::getParentId, dto.getParentId())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同书籍类别，请重新输入");
        }
    }

}
