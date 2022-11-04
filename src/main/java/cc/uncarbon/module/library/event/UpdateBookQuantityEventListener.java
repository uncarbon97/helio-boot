package cc.uncarbon.module.library.event;


import cc.uncarbon.module.library.entity.BookEntity;
import cc.uncarbon.module.library.enums.UpdateBookQuantityEventTypeEnum;
import cc.uncarbon.module.library.service.BookBorrowingService;
import cc.uncarbon.module.library.service.BookDamageService;
import cc.uncarbon.module.library.service.BookService;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;

/**
 * 异步更新书籍存量事件观察者
 *
 * @author Uncarbon
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class UpdateBookQuantityEventListener {

    private final BookService bookService;
    private final BookBorrowingService bookBorrowingService;
    private final BookDamageService bookDamageService;

    @EventListener
    @Async(value = "taskExecutor")
    public void onUpdateBookQuantity(UpdateBookQuantityEvent event) {
        log.info("异步更新书籍存量 >> {}", event);

        // 一个对象循环利用
        BookEntity template = new BookEntity();
        for (Long bookId : event.getData().getBookIds()) {
            // 避免残留影响
            template
                    .setBorrowedQuantity(null)
                    .setDamagedQuantity(null)
            ;

            switch (event.getData().getEventType()) {
                case UPDATE_BORROWING_QUANTITY: {
                    // 查询并赋值被借阅总数量
                    Integer quantityTotal = bookBorrowingService.getBorrowingQuantityTotal(bookId);
                    template.setBorrowedQuantity(quantityTotal);
                    break;
                }
                case UPDATE_DAMAGED_QUANTITY: {
                    // 查询并赋值被损坏总数量
                    Integer quantityTotal = bookDamageService.getDamagedQuantityTotal(bookId);
                    template.setDamagedQuantity(quantityTotal);
                    break;
                }
            }

            template.setId(bookId);
            bookService.updateById(template);
        }
    }

    /**
     * 事件对象
     */
    @Getter
    @ToString
    public static class UpdateBookQuantityEvent extends ApplicationEvent {

        private final UpdateBookQuantityEventData data;

        public UpdateBookQuantityEvent(UpdateBookQuantityEventData data) {
            super(data);
            this.data = data;
        }
    }

    /**
     * 事件附件数据
     */
    @Accessors(chain = true)
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UpdateBookQuantityEventData implements Serializable {

        @ApiModelProperty(value = "书籍ID集合")
        private Set<Long> bookIds;

        @ApiModelProperty(value = "事件类型")
        private UpdateBookQuantityEventTypeEnum eventType;

    }
}
