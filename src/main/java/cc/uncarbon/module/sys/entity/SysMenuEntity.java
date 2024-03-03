package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.sys.enums.SysMenuTypeEnum;
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
 * 后台菜单
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_menu")
public class SysMenuEntity extends HelioBaseEntity<Long> {

	@Schema(description = "名称")
	@TableField(value = "title")
	private String title;

	@Schema(description = "上级菜单ID")
	@TableField(value = "parent_id")
	private Long parentId;

	@Schema(description = "菜单类型")
	@TableField(value = "type")
	private SysMenuTypeEnum type;

	@Schema(description = "权限标识")
	@TableField(value = "permission")
	private String permission;

	@Schema(description = "图标")
	@TableField(value = "icon")
	private String icon;

	@Schema(description = "排序")
	@TableField(value = "sort")
	private Integer sort;

	@Schema(description = "状态")
	@TableField(value = "status")
	private EnabledStatusEnum status;

	@Schema(description = "组件(Vue项目中`/@/views/`后的路径部分; 填`LAYOUT`为空页面)")
	@TableField(value = "component")
	private String component;

	@Schema(description = "外链地址")
	@TableField(value = "external_link")
	private String externalLink;

}
