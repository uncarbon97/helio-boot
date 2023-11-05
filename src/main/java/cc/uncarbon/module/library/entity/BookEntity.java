package cc.uncarbon.module.library.entity;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
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

import java.math.BigDecimal;


/**
 * 书籍
 *
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "tb_book")
public class BookEntity extends HelioBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "书籍名")
    @TableField(value = "title")
    private String title;

    @ApiModelProperty(value = "ISBN号")
    @TableField(value = "isbn")
    private String isbn;

    @ApiModelProperty(value = "出版社")
    @TableField(value = "publisher")
    private String publisher;

    @ApiModelProperty(value = "作者名")
    @TableField(value = "author")
    private String author;

    @ApiModelProperty(value = "所属书籍类别ID")
    @TableField(value = "book_classified_id")
    private Long bookClassifiedId;

    @ApiModelProperty(value = "单价")
    @TableField(value = "unit_price")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "状态")
    @TableField(value = "status")
    private EnabledStatusEnum status;

    @ApiModelProperty(value = "封面图片URL")
    @TableField(value = "cover_img_url")
    private String coverImgUrl;

    @ApiModelProperty(value = "描述")
    @TableField(value = "description")
    private String description;

    @ApiModelProperty(value = "总数量")
    @TableField(value = "total_quantity")
    private Integer totalQuantity;

    @ApiModelProperty(value = "被借阅数量")
    @TableField(value = "borrowed_quantity")
    private Integer borrowedQuantity;

    @ApiModelProperty(value = "被损坏数量")
    @TableField(value = "damaged_quantity")
    private Integer damagedQuantity;

}
