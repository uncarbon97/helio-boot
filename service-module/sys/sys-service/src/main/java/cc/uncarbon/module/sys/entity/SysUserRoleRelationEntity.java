package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.crud.entity.HelioNoTenantBaseEntity;
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
 * 后台用户-角色关联
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_user_role_relation")
public class SysUserRoleRelationEntity extends HelioNoTenantBaseEntity<Long> {

	@ApiModelProperty(value = "用户ID")
	@TableField(value = "user_id")
	private Long userId;

	@ApiModelProperty(value = "角色ID")
	@TableField(value = "role_id")
	private Long roleId;

}
