package cc.uncarbon.module.library.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.sys.enums.GenericStatusEnum;
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
 * 书籍类别
 *
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "tb_book_classified")
public class BookClassifiedEntity extends HelioBaseEntity<Long> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "名称")
    @TableField(value = "title")
    private String title;

    @ApiModelProperty(value = "状态")
    @TableField(value = "status")
    private GenericStatusEnum status;

    @ApiModelProperty(value = "描述")
    @TableField(value = "description")
    private String description;

    @ApiModelProperty(value = "父类别ID")
    @TableField(value = "parent_id")
    private Long parentId;

    @ApiModelProperty(value = "排序")
    @TableField(value = "sort")
    private Integer sort;

}
