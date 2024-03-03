package cc.uncarbon.module.sys.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;


/**
 * 后台管理-重置某用户密码
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminResetSysUserPasswordDTO implements Serializable {

    @Schema(description = "随机新密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 16, max = 64, message = "【随机新密码】最短16位，最长64位")
    @NotBlank(message = "随机新密码不能为空")
    private String randomPassword;

    @Schema(description = "用户ID", hidden = true)
    private Long userId;

}
