package cc.uncarbon.module.sys.entity;

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

	@ApiModelProperty(value = "驼峰式键名")
	@TableField(value = "camel_case_key")
	private String camelCaseKey;

	@ApiModelProperty(value = "下划线式键名")
	@TableField(value = "under_case_key")
	private String underCaseKey;

	@ApiModelProperty(value = "帕斯卡式键名")
	@TableField(value = "pascal_case_key")
	private String pascalCaseKey;

	@ApiModelProperty(value = "键值")
	@TableField(value = "value")
	private String value;

	@ApiModelProperty(value = "参数描述")
	@TableField(value = "description")
	private String description;

	@ApiModelProperty(value = "单位")
	@TableField(value = "unit")
	private String unit;

	@ApiModelProperty(value = "取值范围")
	@TableField(value = "value_range")
	private String valueRange;

	@ApiModelProperty(value = "别称键名")
	@TableField(value = "alias_key")
	private String aliasKey;

}
