package cc.uncarbon.module.library.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 后台管理-新增/编辑书籍借阅记录 DTO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateBookBorrowingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "会员ID", required = true)
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @ApiModelProperty(value = "会员学号/工号", required = true)
    @NotBlank(message = "会员学号/工号不能为空")
    private String memberUsername;

    @ApiModelProperty(value = "会员真实姓名", required = true)
    @NotBlank(message = "会员真实姓名不能为空")
    private String memberRealName;

    @ApiModelProperty(value = "书籍ID", required = true)
    @NotNull(message = "书籍ID不能为空")
    private Long bookId;

    @ApiModelProperty(value = "书籍名", required = true)
    @NotBlank(message = "书籍名不能为空")
    private String bookTitle;

    @ApiModelProperty(value = "书籍ISBN号", required = true)
    @NotBlank(message = "书籍ISBN号不能为空")
    private String bookIsbn;

    @ApiModelProperty(value = "借阅数量", required = true)
    @NotNull(message = "借阅数量不能为空")
    private Integer quantity;

    @ApiModelProperty(value = "借阅时间", required = true)
    @NotNull(message = "借阅时间不能为空")
    private LocalDateTime borrowAt;

    @ApiModelProperty(value = "借阅时长(天)", required = true)
    @NotNull(message = "借阅时长(天)不能为空")
    private Integer borrowDuration;

    @ApiModelProperty(value = "约定归还时间", required = true)
    @NotNull(message = "约定归还时间不能为空")
    private LocalDate appointedReturnAt;

}
