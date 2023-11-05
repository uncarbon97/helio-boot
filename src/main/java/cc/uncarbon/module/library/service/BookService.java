package cc.uncarbon.module.library.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.library.entity.BookEntity;
import cc.uncarbon.module.library.mapper.BookMapper;
import cc.uncarbon.module.library.model.request.AdminInsertOrUpdateBookDTO;
import cc.uncarbon.module.library.model.request.AdminListBookDTO;
import cc.uncarbon.module.library.model.response.BookBO;
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
 * 书籍
 *
 * @author Uncarbon
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookService extends HelioBaseServiceImpl<BookMapper, BookEntity> {

    private final BookDamageService bookDamageService;


    /**
     * 后台管理-分页列表
     */
    public PageResult<BookBO> adminList(PageParam pageParam, AdminListBookDTO dto) {
        Page<BookEntity> entityPage = this.page(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<BookEntity>()
                        .lambda()
                        // 书籍名
                        .like(StrUtil.isNotBlank(dto.getTitle()), BookEntity::getTitle, StrUtil.cleanBlank(dto.getTitle()))
                        // 作者名
                        .like(StrUtil.isNotBlank(dto.getAuthor()), BookEntity::getAuthor, StrUtil.cleanBlank(dto.getAuthor()))
                        // 所属书籍类别ID
                        .eq(ObjectUtil.isNotNull(dto.getBookClassifiedId()), BookEntity::getBookClassifiedId, dto.getBookClassifiedId())
                        // 状态
                        .eq(ObjectUtil.isNotNull(dto.getStatus()), BookEntity::getStatus, dto.getStatus())
                        // 排序
                        .orderByDesc(BookEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public BookBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public BookBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        BookEntity entity = this.getById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateBookDTO dto) {
        log.info("[后台管理-新增书籍] >> DTO={}", dto);
        this.checkExistence(dto);
        this.processAdminInsertOrUpdateBookDTO(dto);

        dto.setId(null);
        BookEntity entity = new BookEntity();
        BeanUtil.copyProperties(dto, entity);

        this.save(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateBookDTO dto) {
        log.info("[后台管理-编辑书籍] >> DTO={}", dto);
        this.checkExistence(dto);
        this.processAdminInsertOrUpdateBookDTO(dto);

        BookEntity entity = new BookEntity();
        BeanUtil.copyProperties(dto, entity);

        this.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除书籍] >> ids={}", ids);
        this.removeByIds(ids);
    }

    /**
     * 后台管理-列表-下拉框专用
     */
    public List<BookBO> adminListOptions() {
        List<BookEntity> entityList = this.list(
                new QueryWrapper<BookEntity>()
                        .lambda()
                        // 只 SELECT 特定字段
                        .select(BookEntity::getId, BookEntity::getTitle, BookEntity::getIsbn)
                        // 仅启用状态
                        .eq(BookEntity::getStatus, EnabledStatusEnum.ENABLED)
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
    private BookBO entity2BO(BookEntity entity) {
        if (entity == null) {
            return null;
        }

        BookBO bo = new BookBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        if (StrUtil.isNotBlank(bo.getDescription())) {
            // \n 替换为换行符
            bo.setDescription(
                    StrUtil.replace(bo.getDescription(), "\\n", "\n")
            );
        }

        return bo;
    }

    /**
     * 实体 List 转 BO List
     *
     * @param entityList 实体 List
     * @return BO List
     */
    private List<BookBO> entityList2BOs(List<BookEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<BookBO> ret = new ArrayList<>(entityList.size());
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
    private PageResult<BookBO> entityPage2BOPage(Page<BookEntity> entityPage) {
        PageResult<BookBO> ret = new PageResult<>();
        BeanUtil.copyProperties(entityPage, ret);
        ret.setRecords(this.entityList2BOs(entityPage.getRecords()));

        return ret;
    }

    /**
     * 检查是否已存在同名数据
     *
     * @param dto DTO
     */
    private void checkExistence(@NonNull AdminInsertOrUpdateBookDTO dto) {
        BookEntity existingEntity = this.getOne(
                new QueryWrapper<BookEntity>()
                        .lambda()
                        .select(BookEntity::getId)
                        .eq(BookEntity::getIsbn, dto.getIsbn())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同 ISBN 号书籍，请重新输入");
        }
    }

    /**
     * 入库前预处理 DTO
     * @param dto DTO
     */
    private void processAdminInsertOrUpdateBookDTO(@NonNull AdminInsertOrUpdateBookDTO dto) {
        if (StrUtil.isNotBlank(dto.getDescription())) {
            // 换行符替换为"\n"
            dto.setDescription(
                    StrUtil.replace(dto.getDescription(), "\n", "\\n")
            );
        }
    }

}
