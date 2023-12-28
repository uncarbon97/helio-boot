package cc.uncarbon.module.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 后台管理-修改当前用户密码
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminUpdateCurrentSysUserPasswordDTO implements Serializable {

    @ApiModelProperty(value = "原密码", required = true)
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true)
    @Size(min = 8, max = 20, message = "密码须为8-20位")
    @NotBlank(message = "密码不能为空")
    private String newPassword;

    @ApiModelProperty(value = "确认新密码", required = true)
    @Size(min = 8, max = 20, message = "确认密码须为8-20位")
    @NotBlank(message = "确认密码不能为空")
    private String confirmNewPassword;

}
