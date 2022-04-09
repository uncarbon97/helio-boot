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
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_dept")
public class SysDeptEntity extends HelioBaseEntity<Long> {

	/**
	 * 乐观锁
	 * 需自行加@Version注解才有效
	 */
	@ApiModelProperty(value = "乐观锁", notes = "需再次复制本字段，并自行加 @Version 注解才有效")
	@TableField(value = "revision", exist = false)
	private Long revision;

	@ApiModelProperty(value = "名称")
	@TableField(value = "title")
	private String title;

	@ApiModelProperty(value = "上级ID(无上级节点设置为0)")
	@TableField(value = "parent_id")
	private Long parentId;

	@ApiModelProperty(value = "排序")
	@TableField(value = "sort")
	private Integer sort;

	@ApiModelProperty(value = "状态")
	@TableField(value = "status")
	private GenericStatusEnum status;

}
