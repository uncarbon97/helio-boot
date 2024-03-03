package cc.uncarbon.module.sys.entity;

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


/**
 * 数据字典
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_data_dict")
public class SysDataDictEntity extends HelioBaseEntity<Long> {

	@Schema(description = "驼峰式键名")
	@TableField(value = "camel_case_key")
	private String camelCaseKey;

	@Schema(description = "下划线式键名")
	@TableField(value = "under_case_key")
	private String underCaseKey;

	@Schema(description = "帕斯卡式键名")
	@TableField(value = "pascal_case_key")
	private String pascalCaseKey;

	@Schema(description = "数据值")
	@TableField(value = "value")
	private String value;

	@Schema(description = "描述")
	@TableField(value = "description")
	private String description;

	@Schema(description = "单位")
	@TableField(value = "unit")
	private String unit;

	@Schema(description = "取值范围")
	@TableField(value = "value_range")
	private String valueRange;

	@Schema(description = "别称键名")
	@TableField(value = "alias_key")
	private String aliasKey;

}
