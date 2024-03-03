package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.crud.entity.HelioNoTenantBaseEntity;
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
 * 后台用户-部门关联
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_user_dept_relation")
public class SysUserDeptRelationEntity extends HelioNoTenantBaseEntity<Long> {

	@Schema(description = "用户ID")
	@TableField(value = "user_id")
	private Long userId;

	@Schema(description = "部门ID")
	@TableField(value = "dept_id")
	private Long deptId;

}
