package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.module.sys.enums.SysMenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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

    @Schema(description = "主键ID", hidden = true, title = "仅更新时使用")
    private Long id;

    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 50, message = "【菜单名称】最长50位")
    @NotBlank(message = "菜单名称不能为空")
    private String title;

    @Schema(description = "上级菜单ID(无上级节点设置为0)")
    private Long parentId;

    @Schema(description = "菜单类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "菜单类型不能为空")
    private SysMenuTypeEnum type;

    @Schema(description = "组件")
    @Size(max = 50, message = "【组件】最长50位")
    private String component;

    @Schema(description = "权限标识")
    @Size(max = 255, message = "【权限标识】最长255位")
    private String permission;

    @Schema(description = "图标")
    @Size(max = 255, message = "【图标】最长255位")
    private String icon;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private EnabledStatusEnum status;

    @Schema(description = "外链地址")
    @Size(max = 255, message = "【外链地址】最长255位")
    private String externalLink;

}
