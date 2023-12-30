package cc.uncarbon.module.sys.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


/**
 * 后台菜单子项详情 for VbenAdmin
 */
@AllArgsConstructor
@Data
public class VbenAdminMenuMetaVO implements Serializable {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "是否不可关闭")
    private Boolean affix;

    @ApiModelProperty(value = "图标")
    private String icon;

}
