package cc.uncarbon.module.library.service;

import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.library.entity.BookBorrowingEntity;
import cc.uncarbon.module.library.enums.BookBorrowingQueryPeriodTypeEnum;
import cc.uncarbon.module.library.enums.BookBorrowingStatusEnum;
import cc.uncarbon.module.library.enums.UpdateBookQuantityEventTypeEnum;
import cc.uncarbon.module.library.event.UpdateBookQuantityEventListener;
import cc.uncarbon.module.library.mapper.BookBorrowingMapper;
import cc.uncarbon.module.library.model.request.AdminInsertOrUpdateBookBorrowingDTO;
import cc.uncarbon.module.library.model.request.AdminListBookBorrowingDTO;
import cc.uncarbon.module.library.model.response.BookBorrowingBO;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
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
 * 书籍借阅记录
 *
 * @author Uncarbon
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookBorrowingService extends HelioBaseServiceImpl<BookBorrowingMapper, BookBorrowingEntity> {

    /**
     * 后台管理-分页列表
     */
    public PageResult<BookBorrowingBO> adminList(PageParam pageParam, AdminListBookBorrowingDTO dto) {
        Page<BookBorrowingEntity> entityPage = this.page(
                new Page<>(pageParam.getPageNum(), pageParam.getPageSize()),
                new QueryWrapper<BookBorrowingEntity>()
                        .lambda()
                        // 会员学号/工号
                        .like(StrUtil.isNotBlank(dto.getMemberUsername()), BookBorrowingEntity::getMemberUsername, StrUtil.cleanBlank(dto.getMemberUsername()))
                        // 会员真实姓名
                        .like(StrUtil.isNotBlank(dto.getMemberRealName()), BookBorrowingEntity::getMemberRealName, StrUtil.cleanBlank(dto.getMemberRealName()))
                        // 书籍ID
                        .eq(ObjectUtil.isNotNull(dto.getBookId()), BookBorrowingEntity::getBookId, dto.getBookId())
                        // 状态
                        .eq(ObjectUtil.isNotNull(dto.getStatus()), BookBorrowingEntity::getStatus, dto.getStatus())
                        // 时间区间类型 + 时间区间
                        .and(ObjectUtil.isAllNotEmpty(dto.getQueryPeriodType(), dto.getBeginAt(), dto.getEndAt()),
                                wrapper -> wrapper
                                        // 借阅时间
                                        .between(dto.getQueryPeriodType() == BookBorrowingQueryPeriodTypeEnum.BORROW, BookBorrowingEntity::getBorrowAt, dto.getBeginAt(), dto.getEndAt())
                                        // 约定归还时间
                                        .between(dto.getQueryPeriodType() == BookBorrowingQueryPeriodTypeEnum.APPOINTED_RETURN, BookBorrowingEntity::getAppointedReturnAt, dto.getBeginAt(), dto.getEndAt())
                                        // 实际归还时间
                                        .between(dto.getQueryPeriodType() == BookBorrowingQueryPeriodTypeEnum.ACTUAL_RETURN, BookBorrowingEntity::getActualReturnAt, dto.getBeginAt(), dto.getEndAt())
                        )
                        // 排序
                        .orderByDesc(BookBorrowingEntity::getCreatedAt)
        );

        return this.entityPage2BOPage(entityPage);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public BookBorrowingBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public BookBorrowingBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        BookBorrowingEntity entity = this.getById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     */
    public Long adminInsert(AdminInsertOrUpdateBookBorrowingDTO dto) {
        log.info("[后台管理-新增书籍借阅记录] >> DTO={}", dto);
        this.checkExistence(dto);

        // 为了事务生效
        BookBorrowingService self = SpringUtil.getBean(BookBorrowingService.class);
        Long entityId = self.adminInsertTransaction(dto);

        // 异步更新存量（需要等上方事务提交后再更新）
        SpringUtil.publishEvent(
                new UpdateBookQuantityEventListener.UpdateBookQuantityEvent(
                        UpdateBookQuantityEventListener.UpdateBookQuantityEventData.builder()
                                .bookIds(Collections.singleton(dto.getBookId()))
                                .eventType(UpdateBookQuantityEventTypeEnum.UPDATE_BORROWING_QUANTITY)
                                .build()
                )
        );

        return entityId;
    }

    /**
     * 后台管理-新增-事务过程
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsertTransaction(AdminInsertOrUpdateBookBorrowingDTO dto) {
        dto.setId(null);
        BookBorrowingEntity entity = new BookBorrowingEntity();
        BeanUtil.copyProperties(dto, entity);

        // 新增后为“借阅中状态”
        entity.setStatus(BookBorrowingStatusEnum.BORROWING);

        this.save(entity);

        return entity.getId();
    }

    /**
     * 后台管理-删除
     */
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除书籍借阅记录] >> ids={}", ids);

        if (CollUtil.isEmpty(ids)) {
            return;
        }

        // 先查出对应的书籍ID
        Set<Long> bookIds = this.list(
                new QueryWrapper<BookBorrowingEntity>()
                        .lambda()
                        .select(BookBorrowingEntity::getBookId)
                        .in(BookBorrowingEntity::getId, ids)
        ).stream().map(BookBorrowingEntity::getBookId).collect(Collectors.toSet());

        // 为了事务生效
        BookBorrowingService self = SpringUtil.getBean(BookBorrowingService.class);
        self.adminDeleteTransaction(ids);

        // 异步更新存量（需要等上方事务提交后再更新）
        SpringUtil.publishEvent(
                new UpdateBookQuantityEventListener.UpdateBookQuantityEvent(
                        UpdateBookQuantityEventListener.UpdateBookQuantityEventData.builder()
                                .bookIds(bookIds)
                                .eventType(UpdateBookQuantityEventTypeEnum.UPDATE_BORROWING_QUANTITY)
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
     * 后台管理-确认归还书籍
     */
    public void adminReturn(Long id) {
        BookBorrowingBO bo = this.getOneById(id, true);

        // 为了事务生效
        BookBorrowingService self = SpringUtil.getBean(BookBorrowingService.class);
        self.adminReturnTransaction(id);

        // 异步更新存量（需要等上方事务提交后再更新）
        SpringUtil.publishEvent(
                new UpdateBookQuantityEventListener.UpdateBookQuantityEvent(
                        UpdateBookQuantityEventListener.UpdateBookQuantityEventData.builder()
                                .bookIds(Collections.singleton(bo.getBookId()))
                                .eventType(UpdateBookQuantityEventTypeEnum.UPDATE_BORROWING_QUANTITY)
                                .build()
                )
        );
    }

    /**
     * 后台管理-确认归还书籍-事务过程
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminReturnTransaction(Long id) {
        BookBorrowingEntity template = new BookBorrowingEntity();
        template
                .setStatus(BookBorrowingStatusEnum.RETURNED)
                .setActualReturnAt(LocalDateTimeUtil.now())
                .setId(id)
        ;
        this.updateById(template);
    }

    /**
     * 查询已借阅总数
     *
     * @param bookId 书籍ID
     */
    public Integer getBorrowingQuantityTotal(@NonNull Long bookId) {
        return Optional.ofNullable(this.getOne(
                new QueryWrapper<BookBorrowingEntity>()
                        .select(" SUM(quantity) AS quantity ")
                        .lambda()
                        .eq(BookBorrowingEntity::getBookId, bookId)
                // 查不到则返回0
        )).map(BookBorrowingEntity::getQuantity).orElse(0);
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
    private BookBorrowingBO entity2BO(BookBorrowingEntity entity) {
        if (entity == null) {
            return null;
        }

        BookBorrowingBO bo = new BookBorrowingBO();
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
    private List<BookBorrowingBO> entityList2BOs(List<BookBorrowingEntity> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<BookBorrowingBO> ret = new ArrayList<>(entityList.size());
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
    private PageResult<BookBorrowingBO> entityPage2BOPage(Page<BookBorrowingEntity> entityPage) {
        PageResult<BookBorrowingBO> ret = new PageResult<>();
        BeanUtil.copyProperties(entityPage, ret);
        ret.setRecords(this.entityList2BOs(entityPage.getRecords()));

        return ret;
    }

    /**
     * 检查是否已存在同名数据
     *
     * @param dto DTO
     */
    private void checkExistence(@NonNull AdminInsertOrUpdateBookBorrowingDTO dto) {
        /*
        可以根据自己业务需要，解禁这段代码，修改判断条件和文案

        BookBorrowingEntity existingEntity = this.getOne(
                new QueryWrapper<BookBorrowingEntity>()
                        .lambda()
                        .select(BookBorrowingEntity::getId)
                        .eq(BookBorrowingEntity::getTitle, dto.getTitle())
                        .last(HelioConstant.CRUD.SQL_LIMIT_1)
        );

        if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
            throw new BusinessException(400, "已存在相同书籍借阅记录，请重新输入");
        }
        */
    }

}
