package cc.uncarbon.module.library.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.library.enums.BookBorrowingStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 书籍借阅记录
 *
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "tb_book_borrowing")
public class BookBorrowingEntity extends HelioBaseEntity<Long> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "会员ID")
    @TableField(value = "member_id")
    private Long memberId;

    @ApiModelProperty(value = "会员学号/工号")
    @TableField(value = "member_username")
    private String memberUsername;

    @ApiModelProperty(value = "会员真实姓名")
    @TableField(value = "member_real_name")
    private String memberRealName;

    @ApiModelProperty(value = "书籍ID")
    @TableField(value = "book_id")
    private Long bookId;

    @ApiModelProperty(value = "书籍名")
    @TableField(value = "book_title")
    private String bookTitle;

    @ApiModelProperty(value = "书籍ISBN号")
    @TableField(value = "book_isbn")
    private String bookIsbn;

    @ApiModelProperty(value = "状态")
    @TableField(value = "status")
    private BookBorrowingStatusEnum status;

    @ApiModelProperty(value = "借阅数量")
    @TableField(value = "quantity")
    private Integer quantity;

    @ApiModelProperty(value = "借阅时间")
    @TableField(value = "borrow_at")
    private LocalDateTime borrowAt;

    @ApiModelProperty(value = "借阅时长(天)")
    @TableField(value = "borrow_duration")
    private Integer borrowDuration;

    @ApiModelProperty(value = "约定归还时间")
    @TableField(value = "appointed_return_at")
    private LocalDate appointedReturnAt;

    @ApiModelProperty(value = "实际归还时间")
    @TableField(value = "actual_return_at")
    private LocalDateTime actualReturnAt;

}
