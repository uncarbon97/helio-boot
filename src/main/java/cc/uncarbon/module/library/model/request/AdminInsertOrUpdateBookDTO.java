package cc.uncarbon.module.library.model.request;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 后台管理-新增/编辑书籍 DTO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateBookDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "书籍名", required = true)
    @NotBlank(message = "书籍名不能为空")
    private String title;

    @ApiModelProperty(value = "ISBN号", required = true)
    @NotBlank(message = "ISBN号不能为空")
    private String isbn;

    @ApiModelProperty(value = "出版社", required = true)
    @NotBlank(message = "出版社不能为空")
    private String publisher;

    @ApiModelProperty(value = "作者名", required = true)
    @NotBlank(message = "作者名不能为空")
    private String author;

    @ApiModelProperty(value = "所属书籍类别ID", required = true)
    @NotNull(message = "所属书籍类别ID不能为空")
    private Long bookClassifiedId;

    @ApiModelProperty(value = "单价", required = true)
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "状态不能为空")
    private EnabledStatusEnum status;

    @ApiModelProperty(value = "封面图片URL", required = true)
    @NotBlank(message = "封面图片不能为空")
    private String coverImgUrl;

    @ApiModelProperty(value = "描述", required = true)
    @NotBlank(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "总数量", required = true)
    @NotNull(message = "总数量不能为空")
    private Integer totalQuantity;

}
