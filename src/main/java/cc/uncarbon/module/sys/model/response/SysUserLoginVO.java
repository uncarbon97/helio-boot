package cc.uncarbon.module.sys.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Collection;


/**
 * 登录后返回的字段
 * 用于返回给前端
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserLoginVO implements Serializable {

    @ApiModelProperty(value = "token名称")
    private String tokenName;

    @ApiModelProperty(value = "token值")
    private String tokenValue;

    @ApiModelProperty(value = "对应角色")
    private Collection<String> roles;

    @ApiModelProperty(value = "拥有权限")
    private Collection<String> permissions;

}
