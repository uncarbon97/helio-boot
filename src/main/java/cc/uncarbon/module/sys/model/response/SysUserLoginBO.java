package cc.uncarbon.module.sys.model.response;

import cc.uncarbon.framework.core.context.TenantContext;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


/**
 * 后台用户BO
 * 用于登录后返回字段
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserLoginBO implements Serializable {

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "手机号")
    private String phoneNo;

    @ApiModelProperty(value = "拥有角色")
    private List<String> roles;

    @ApiModelProperty(value = "拥有权限")
    private List<String> permissions;

    @ApiModelProperty("所属租户")
    private TenantContext relationalTenant;

}
