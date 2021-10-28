package cc.uncarbon.module.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;


/**
 * 后台管理-新增/编辑后台角色
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateSysRoleDTO implements Serializable {

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "所属租户ID", hidden = true, notes = "仅新增时使用")
    private Long tenantId;
        
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String title;
        
    @ApiModelProperty(value = "值", required = true)
    @NotBlank(message = "值不能为空")
    private String value;

    @ApiModelProperty(value = "菜单Ids(空=清理关联关系后不再绑定任何菜单)")
    private List<Long> menuIds;

}
