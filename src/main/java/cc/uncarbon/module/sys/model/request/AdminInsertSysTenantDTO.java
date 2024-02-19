package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.constant.HelioConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;


/**
 * 后台管理-新增系统租户
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertSysTenantDTO extends AdminUpdateSysTenantDTO {

    @ApiModelProperty(value = "租户ID(纯数字)", required = true)
    @Positive(message = "租户ID须为正整数")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "管理员账号", required = true)
    @Size(min = 6, max = 16, message = "【管理员账号】最短6位，最长16位")
    @NotBlank(message = "管理员账号不能为空")
    private String tenantAdminUsername;

    @ApiModelProperty(value = "管理员密码", required = true)
    @Size(min = 8, max = 20, message = "【管理员密码】最短8位，最长20位")
    @NotBlank(message = "管理员密码不能为空")
    private String tenantAdminPassword;

    @ApiModelProperty(value = "管理员邮箱", required = true)
    @Pattern(message = "管理员邮箱格式不正确", regexp = HelioConstant.Regex.EMAIL)
    @Size(max = 255, message = "【管理员邮箱】最长255位")
    @NotBlank(message = "管理员邮箱不能为空")
    private String tenantAdminEmail;

    @ApiModelProperty(value = "管理员手机号", required = true)
    @Pattern(message = "管理员手机号格式不正确", regexp = HelioConstant.Regex.CHINA_MAINLAND_PHONE_NO)
    @Size(max = 20, message = "【管理员手机号】最长20位")
    @NotBlank(message = "管理员手机号不能为空")
    private String tenantAdminPhoneNo;

}
