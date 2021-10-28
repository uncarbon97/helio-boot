package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.module.sys.enums.SysMenuTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


/**
 * 后台管理-列表后台菜单
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminListSysMenuDTO implements Serializable {

    @ApiModelProperty(value = "无上级节点名称(关键词)")
    private String title;

    @ApiModelProperty(value = "上级ID(无上级节点设置为0)")
    private Long parentId;

    @ApiModelProperty(value = "【内部使用】菜单类型", hidden = true)
    private List<SysMenuTypeEnum> menuTypes;

}
