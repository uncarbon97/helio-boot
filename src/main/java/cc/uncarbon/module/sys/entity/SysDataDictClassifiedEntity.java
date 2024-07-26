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
 * 数据字典分类
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_data_dict_classified")
public class SysDataDictClassifiedEntity extends HelioBaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 1L;


    @Schema(description = "分类编码")
    @TableField(value = "code")
    private String code;

    @Schema(description = "分类名称")
    @TableField(value = "name")
    private String name;

    @Schema(description = "状态")
    @TableField(value = "status")
    private EnabledStatusEnum status;

    @Schema(description = "分类描述")
    @TableField(value = "description")
    private String description;

}
