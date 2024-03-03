package cc.uncarbon.module.sys.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


/**
 * 后台菜单子项详情 for VbenAdmin
 */
@AllArgsConstructor
@Data
public class VbenAdminMenuMetaVO implements Serializable {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "是否不可关闭")
    private Boolean affix;

    @Schema(description = "图标")
    private String icon;

}
