package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.module.sys.constant.SysConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;


/**
 * 后台管理-新增/编辑后台角色
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

    @ApiModelProperty(value = "角色名", required = true)
    @Size(max = 50, message = "【角色名】最长50位")
    @NotBlank(message = "角色名不能为空")
    private String title;

    @ApiModelProperty(value = "角色编码", required = true)
    @Size(max = 100, message = "【角色编码】最长100位")
    @NotBlank(message = "角色编码不能为空")
    private String value;

    /**
     * 是否用于创建新租户管理员角色
     */
    public boolean creatingNewTenantAdmin() {
        return Objects.nonNull(tenantId) && SysConstant.TENANT_ADMIN_ROLE_VALUE.equalsIgnoreCase(value);
    }

}
