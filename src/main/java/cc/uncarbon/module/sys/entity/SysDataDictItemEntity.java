package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serial;


/**
 * 数据字典项
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_data_dict_item")
public class SysDataDictItemEntity extends HelioBaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 1L;


    @Schema(description = "所属分类ID")
    @TableField(value = "classified_id")
    private Long classifiedId;

    @Schema(description = "字典项编码")
    @TableField(value = "code")
    private String code;

    @Schema(description = "字典项标签")
    @TableField(value = "label")
    private String label;

    @Schema(description = "字典项值")
    @TableField(value = "value")
    private String value;

    @Schema(description = "状态")
    @TableField(value = "status")
    private EnabledStatusEnum status;

    @Schema(description = "排序")
    @TableField(value = "sort")
    private Integer sort;

    @Schema(description = "描述")
    @TableField(value = "description")
    private String description;

}
