package cc.uncarbon.module.library.service;

import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.library.entity.BookDamageEntity;
import cc.uncarbon.module.library.enums.UpdateBookQuantityEventTypeEnum;
import cc.uncarbon.module.library.event.UpdateBookQuantityEventListener;
import cc.uncarbon.module.library.mapper.BookDamageMapper;
import cc.uncarbon.module.library.model.request.AdminInsertOrUpdateBookDamageDTO;
import cc.uncarbon.module.library.model.request.AdminListBookDamageDTO;
import cc.uncarbon.module.library.model.response.BookDamageBO;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 书籍损坏记录
 *
 * @author Uncarbon
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookDamageService extends HelioBaseServiceImpl<BookDamageMapper, BookDamageEntity> {


    /**
     * 后台管理-分页列表
     */
    public PageResult<BookDamageBO> adminList(PageParam pageParam, AdminListBookDamageDTO dto) {
        Page<BookDamageEntity> entityPage = this.page(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<BookDamageEntity>()
                        .lambda()
                        // 书籍名
                        .like(StrUtil.isNotBlank(dto.getBookTitle()), BookDamageEntity::getBookTitle, StrUtil.cleanBlank(dto.getBookTitle()))
                         // 排序
                        .orderByDesc(BookDamageEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public BookDamageBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id               主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public BookDamageBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        BookDamageEntity entity = this.getById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    public Long adminInsert(AdminInsertOrUpdateBookDamageDTO dto) {
        log.info("[后台管理-新增书籍损坏记录] >> DTO={}", dto);
        this.checkExistence(dto);

        // 为了事务生效
        BookDamageService self = SpringUtil.getBean(BookDamageService.class);
        Long entityId = self.adminInsertTransaction(dto);

        // 异步更新存量（需要等上方事务提交后再更新）
        SpringUtil.publishEvent(
                new UpdateBookQuantityEventListener.UpdateBookQuantityEvent(
                        UpdateBookQuantityEventListener.UpdateBookQuantityEventData.builder()
                                .bookIds(Collections.singleton(dto.getBookId()))
                                .eventType(UpdateBookQuantityEventTypeEnum.UPDATE_DAMAGED_QUANTITY)
                                .build()
                )
        );

        return entityId;
    }

    /**
     * 后台管理-新增-事务过程
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsertTransaction(AdminInsertOrUpdateBookDamageDTO dto) {
        dto.setId(null);
        BookDamageEntity entity = new BookDamageEntity();
        BeanUtil.copyProperties(dto, entity);

        this.save(entity);

        return entity.getId();
    }

    /**
     * 后台管理-删除
     */
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除书籍损坏记录] >> ids={}", ids);

        if (CollUtil.isEmpty(ids)) {
            return;
        }

        // 先查出对应的书籍ID
        Set<Long> bookIds = this.list(
                new QueryWrapper<BookDamageEntity>()
                        .lambda()
                        .select(BookDamageEntity::getBookId)
                        .in(BookDamageEntity::getId, ids)
        ).stream().map(BookDamageEntity::getBookId).collect(Collectors.toSet());

        // 为了事务生效
        BookDamageService self = SpringUtil.getBean(BookDamageService.class);
        self.adminDeleteTransaction(ids);

        // 异步更新存量（需要等上方事务提交后再更新）
        SpringUtil.publishEvent(
                new UpdateBookQuantityEventListener.UpdateBookQuantityEvent(
                        UpdateBookQuantityEventListener.UpdateBookQuantityEventData.builder()
                                .bookIds(bookIds)
                                .eventType(UpdateBookQuantityEventTypeEnum.UPDATE_DAMAGED_QUANTITY)
                                .build()
                )
        );
    }

    /**
     * 后台管理-删除-事务过程
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDeleteTransaction(Collection<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 查询已报损总数
     *
     * @param bookId 书籍ID
     */
    public Integer getDamagedQuantityTotal(@NonNull Long bookId) {
        return Optional.ofNullable(this.getOne(
                new QueryWrapper<BookDamageEntity>()
                        .select(" SUM(quantity) AS quantity ")
                        .lambda()
                        .eq(BookDamageEntity::getBookId, bookId)
                // 查不到则返回0
        )).map(BookDamageEntity::getQuantity).orElse(0);
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
    private BookDamageBO entity2BO(BookDamageEntity entity) {
        if (entity == null) {
            return null;
        }

        BookDamageBO bo = new BookDamageBO();
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
    private List<BookDamageBO> entityList2BOs(List<BookDamageEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<BookDamageBO> ret = new ArrayList<>(entityList.size());
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
    private PageResult<BookDamageBO> entityPage2BOPage(Page<BookDamageEntity> entityPage) {
        PageResult<BookDamageBO> ret = new PageResult<>();
        BeanUtil.copyProperties(entityPage, ret);
        ret.setRecords(this.entityList2BOs(entityPage.getRecords()));

        return ret;
    }

    /**
     * 检查是否已存在同名数据
     *
     * @param dto DTO
     */
    private void checkExistence(@NonNull AdminInsertOrUpdateBookDamageDTO dto) {
        /*
        可以根据自己业务需要，解禁这段代码，修改判断条件和文案

        BookDamageEntity existingEntity = this.getOne(
                new QueryWrapper<BookDamageEntity>()
                        .lambda()
                        .select(BookDamageEntity::getId)
                        .eq(BookDamageEntity::getTitle, dto.getTitle())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同书籍损坏记录，请重新输入");
        }
        */
    }

}
