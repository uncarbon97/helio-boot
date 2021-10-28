package cc.uncarbon.module.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 后台管理-后台用户登录
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserLoginDTO implements Serializable {

    @ApiModelProperty(value = "账号", required = true)
    @Size(min = 5, max = 16, message = "账号须为5-16位")
    @NotBlank(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @Size(min = 5, max = 64, message = "密码须为5-64位")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "记住我", required = true)
    @NotNull(message = "记住我不能为空")
    private Boolean rememberMe;

}
