package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.sys.constant.SysConstant;
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
 * 后台角色
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_role")
public class SysRoleEntity extends HelioBaseEntity<Long> {

	@ApiModelProperty(value = "角色名")
	@TableField(value = "title")
	private String title;

	@ApiModelProperty(value = "角色编码")
	@TableField(value = "value")
	private String value;

	/**
	 * 角色实例可被视为超级管理员
	 */
	public boolean isSuperAdmin() {
		return SysConstant.SUPER_ADMIN_ROLE_ID.equals(getId()) || SysConstant.SUPER_ADMIN_ROLE_VALUE.equalsIgnoreCase(getValue());
	}

	/**
	 * 角色实例可被视为租户管理员
	 */
	public boolean isTenantAdmin() {
		return SysConstant.TENANT_ADMIN_ROLE_VALUE.equalsIgnoreCase(getValue());
	}

}
