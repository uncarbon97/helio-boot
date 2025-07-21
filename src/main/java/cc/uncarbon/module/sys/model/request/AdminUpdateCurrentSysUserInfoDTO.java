package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 后台管理-更新当前后台用户信息资料
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminUpdateCurrentSysUserInfoDTO implements Serializable {


    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 100, message = "【昵称】最长100位")
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @Schema(description = "性别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "性别不能为空")
    private GenderEnum gender;

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(message = "邮箱格式有误", regexp = HelioConstant.Regex.EMAIL)
    @Size(max = 255, message = "【邮箱】最长255位")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(message = "手机号格式有误", regexp = HelioConstant.Regex.CHINA_MAINLAND_PHONE_NO)
    @Size(max = 20, message = "【手机号】最长20位")
    @NotBlank(message = "手机号不能为空")
    private String phoneNo;

    @Schema(description = "头像URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "头像格式有误")
    private String avatar;

}
