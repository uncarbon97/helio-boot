package cc.uncarbon.module.sys.model.response;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.module.sys.enums.SysMenuTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 后台菜单BO
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysMenuBO implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "创建时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    @Schema(description = "更新时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;

    @Schema(description = "名称")
    private String title;

    @Schema(description = "上级菜单ID")
    private Long parentId;

    @Schema(description = "菜单类型")
    private SysMenuTypeEnum type;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private EnabledStatusEnum status;

    @Schema(description = "组件")
    private String component;

    @Schema(description = "外链地址")
    private String externalLink;

    @Schema(description = "【用于Vben Admin】路由地址", hidden = true)
    private String path;

    @Schema(description = "【用于Vben Admin】菜单名(全局唯一, 不能重复)", hidden = true)
    private String name;

    @Schema(description = "【用于Vben Admin】菜单详情", hidden = true)
    private VbenAdminMenuMetaVO meta;

}
