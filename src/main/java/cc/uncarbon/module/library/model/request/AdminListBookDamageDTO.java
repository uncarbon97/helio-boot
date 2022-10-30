package cc.uncarbon.module.library.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


/**
 * 后台管理-分页列表书籍损坏记录 DTO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminListBookDamageDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "书籍名(关键词)")
    private String bookTitle;

}
