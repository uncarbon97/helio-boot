package cc.uncarbon.module.library.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;


/**
 * 书籍损坏记录
 *
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "tb_book_damage")
public class BookDamageEntity extends HelioBaseEntity<Long> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "书籍ID")
    @TableField(value = "book_id")
    private Long bookId;

    @ApiModelProperty(value = "书籍名")
    @TableField(value = "book_title")
    private String bookTitle;

    @ApiModelProperty(value = "报损数量")
    @TableField(value = "quantity")
    private Integer quantity;

    @ApiModelProperty(value = "备注")
    @TableField(value = "remark")
    private String remark;

}
