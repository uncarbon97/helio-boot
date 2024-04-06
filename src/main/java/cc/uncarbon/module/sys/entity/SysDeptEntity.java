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


/**
 * 部门
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
	@Schema(description = "乐观锁", title = "需再次复制本字段，并自行加 @Version 注解才有效")
	@TableField(value = "revision", exist = false)
	private Long revision;

	@Schema(description = "名称")
	@TableField(value = "title")
	private String title;

	@Schema(description = "上级ID(无上级节点设置为0)")
	@TableField(value = "parent_id")
	private Long parentId;

	@Schema(description = "排序")
	@TableField(value = "sort")
	private Integer sort;

	@Schema(description = "状态")
	@TableField(value = "status")
	private EnabledStatusEnum status;

}
