package cc.uncarbon.module.library.model.request;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


/**
 * 后台管理-分页列表书籍 DTO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminListBookDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "书籍名(关键词)")
    private String title;

    @ApiModelProperty(value = "作者名(关键词)")
    private String author;

    @ApiModelProperty(value = "所属书籍类别ID")
    private Long bookClassifiedId;

    @ApiModelProperty(value = "状态")
    private EnabledStatusEnum status;

}
