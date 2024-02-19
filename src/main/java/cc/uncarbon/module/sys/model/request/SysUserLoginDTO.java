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
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserLoginDTO implements Serializable {

    @ApiModelProperty(value = "账号", required = true)
    @Size(min = 5, max = 16, message = "【账号】最短5位，最长16位")
    @NotBlank(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @Size(min = 5, max = 20, message = "【密码】最短5位，最长20位")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "记住我", required = true)
    @NotNull(message = "记住我不能为空")
    private Boolean rememberMe;

    @ApiModelProperty(value = "租户ID(可选，启用多租户后有效)")
    private Long tenantId;

    @ApiModelProperty(value = "验证码唯一标识(可选)")
    private String captchaId;

    @ApiModelProperty(value = "验证码答案(可选)")
    private String captchaAnswer;

}
