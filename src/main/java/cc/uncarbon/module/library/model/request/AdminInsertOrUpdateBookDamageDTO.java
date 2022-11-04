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


/**
 * 后台管理-新增/编辑书籍损坏记录 DTO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateBookDamageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "书籍ID", required = true)
    @NotNull(message = "书籍ID不能为空")
    private Long bookId;

    @ApiModelProperty(value = "书籍名", required = true)
    @NotBlank(message = "书籍名不能为空")
    private String bookTitle;

    @ApiModelProperty(value = "报损数量", required = true)
    @NotNull(message = "报损数量不能为空")
    private Integer quantity;

    @ApiModelProperty(value = "备注")
    private String remark;

}
