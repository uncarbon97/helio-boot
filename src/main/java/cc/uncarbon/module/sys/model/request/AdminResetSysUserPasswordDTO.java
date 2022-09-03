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
 * 后台管理-重置某用户密码
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminResetSysUserPasswordDTO implements Serializable {

    @ApiModelProperty(value = "随机新密码", required = true)
    @Size(min = 16, max = 64, message = "随机新密码须为16-64位")
    @NotBlank(message = "随机新密码不能为空")
    private String randomPassword;

    @ApiModelProperty(value = "用户ID", hidden  = true)
    private Long userId;

}
