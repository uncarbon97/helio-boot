package cc.uncarbon.module.sys.model.response;

import cc.uncarbon.framework.core.context.TenantContext;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 登录后返回的字段
 * 用于内部 RPC 调用
 */
@Accessors(chain = true)
@SuperBuilder
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

    @ApiModelProperty(value = "对应角色ID")
    private Set<Long> roleIds;

    @ApiModelProperty(value = "对应角色名")
    private List<String> roles;

    @ApiModelProperty(value = "所有拥有权限名")
    private Set<String> permissions;

    @ApiModelProperty(value = "角色ID-对应权限名 Map")
    private Map<Long, Set<String>> roleIdPermissionMap;

    @ApiModelProperty(value = "关联租户上下文")
    private TenantContext tenantContext;

}
