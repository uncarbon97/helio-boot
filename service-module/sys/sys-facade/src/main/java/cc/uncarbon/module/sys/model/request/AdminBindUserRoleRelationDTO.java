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
 * 后台管理-绑定用户与角色关联关系
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBindUserRoleRelationDTO implements Serializable {

    @ApiModelProperty(value = "角色Ids(空=清理关联关系后不再绑定任何角色)")
    private Set<Long> roleIds;

    @ApiModelProperty(value = "用户ID", hidden = true)
    private Long userId;

}
