package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.module.sys.enums.SysMenuTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 后台管理-新增/编辑后台菜单
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateSysMenuDTO implements Serializable {

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "菜单名称", required = true)
    @Size(max = 50, message = "【菜单名称】最长50位")
    @NotBlank(message = "菜单名称不能为空")
    private String title;

    @ApiModelProperty(value = "上级菜单ID(无上级节点设置为0)")
    private Long parentId;

    @ApiModelProperty(value = "菜单类型", required = true)
    @NotNull(message = "菜单类型不能为空")
    private SysMenuTypeEnum type;

    @ApiModelProperty(value = "组件")
    @Size(max = 50, message = "【组件】最长50位")
    private String component;

    @ApiModelProperty(value = "权限标识")
    @Size(max = 255, message = "【权限标识】最长255位")
    private String permission;

    @ApiModelProperty(value = "图标")
    @Size(max = 255, message = "【图标】最长255位")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态")
    private EnabledStatusEnum status;

    @ApiModelProperty(value = "外链地址")
    @Size(max = 255, message = "【外链地址】最长255位")
    private String externalLink;

}
