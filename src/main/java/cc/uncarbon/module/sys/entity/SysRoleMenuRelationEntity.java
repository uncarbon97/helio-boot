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
 * 后台角色-可见菜单关联
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_role_menu_relation")
public class SysRoleMenuRelationEntity extends HelioBaseEntity<Long> {

	@ApiModelProperty(value = "角色ID")
	@TableField(value = "role_id")
	private Long roleId;

	@ApiModelProperty(value = "菜单ID")
	@TableField(value = "menu_id")
	private Long menuId;

}
