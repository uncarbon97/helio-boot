package cc.uncarbon.module.sys.entity;

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
 * 部门
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_dept")
public class SysDeptEntity extends HelioBaseEntity<Long> {

	@ApiModelProperty(value = "名称")
	@TableField(value = "title")
	private String title;

	@ApiModelProperty(value = "上级ID(无上级节点设置为0)")
	@TableField(value = "parent_id")
	private Long parentId;

	@ApiModelProperty(value = "排序")
	@TableField(value = "sort")
	private Integer sort;

	@ApiModelProperty(value = "状态(0=禁用 1=启用)")
	@TableField(value = "status")
	private GenericStatusEnum status;

}
