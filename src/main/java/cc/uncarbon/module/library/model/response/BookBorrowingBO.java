package cc.uncarbon.module.library.model.response;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.module.library.enums.BookBorrowingStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 书籍借阅记录 BO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookBorrowingBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "创建时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    @ApiModelProperty(value = "会员学号/工号")
    private String memberUsername;

    @ApiModelProperty(value = "会员真实姓名")
    private String memberRealName;

    @ApiModelProperty(value = "书籍ID")
    private Long bookId;

    @ApiModelProperty(value = "书籍名")
    private String bookTitle;

    @ApiModelProperty(value = "书籍ISBN号")
    private String bookIsbn;

    @ApiModelProperty(value = "状态")
    private BookBorrowingStatusEnum status;

    @ApiModelProperty(value = "借阅数量")
    private Integer quantity;

    @ApiModelProperty(value = "借阅时间")
    private LocalDateTime borrowAt;

    @ApiModelProperty(value = "借阅时长(天)")
    private Integer borrowDuration;

    @ApiModelProperty(value = "约定归还时间")
    private LocalDate appointedReturnAt;

    @ApiModelProperty(value = "实际归还时间")
    private LocalDateTime actualReturnAt;

}
