package cc.uncarbon.module.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 后台管理-绑定角色与菜单关联关系
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBindRoleMenuRelationDTO implements Serializable {

    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @ApiModelProperty(value = "菜单Ids(空=清理关联关系后不再绑定任何菜单)")
    private Set<Long> menuIds;

}
