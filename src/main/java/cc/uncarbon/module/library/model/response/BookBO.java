package cc.uncarbon.module.library.model.response;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.module.sys.enums.GenericStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 书籍 BO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookBO implements Serializable {

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

    @ApiModelProperty(value = "书籍名")
    private String title;

    @ApiModelProperty(value = "ISBN号")
    private String isbn;

    @ApiModelProperty(value = "出版社")
    private String publisher;

    @ApiModelProperty(value = "作者名")
    private String author;

    @ApiModelProperty(value = "所属书籍类别ID")
    private Long bookClassifiedId;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "状态")
    private GenericStatusEnum status;

    @ApiModelProperty(value = "封面图片URL")
    private String coverImgUrl;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "总数量")
    private Integer totalQuantity;

    @ApiModelProperty(value = "被借阅数量")
    private Integer borrowedQuantity;

    @ApiModelProperty(value = "被损坏数量")
    private Integer damagedQuantity;

}
