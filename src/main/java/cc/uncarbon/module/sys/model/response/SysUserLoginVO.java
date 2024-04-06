package cc.uncarbon.module.sys.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "token名称")
    private String tokenName;

    @Schema(description = "token值")
    private String tokenValue;

    @Schema(description = "对应角色")
    private Collection<String> roles;

    @Schema(description = "拥有权限")
    private Collection<String> permissions;

}
