package cc.uncarbon.module.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;


/**
 * 后台管理-绑定角色与菜单关联关系
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBindRoleMenuRelationDTO implements Serializable {

    @ApiModelProperty(value = "菜单Ids(空=清理关联关系后不再绑定任何菜单)")
    private Set<Long> menuIds;

    @ApiModelProperty(value = "角色ID", hidden = true)
    private Long roleId;

}
