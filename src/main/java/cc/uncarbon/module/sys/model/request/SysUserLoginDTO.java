package cc.uncarbon.module.sys.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 5, max = 16, message = "【账号】最短5位，最长16位")
    @NotBlank(message = "账号不能为空")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 5, max = 20, message = "【密码】最短5位，最长20位")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "记住我", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "记住我不能为空")
    private Boolean rememberMe;

    @Schema(description = "租户ID(可选，启用多租户后有效)")
    private Long tenantId;

    @Schema(description = "验证码唯一标识(可选)")
    private String captchaId;

    @Schema(description = "验证码答案(可选)")
    private String captchaAnswer;

}
