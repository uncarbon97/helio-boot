package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.constant.HelioConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * 后台管理-新增系统租户
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertSysTenantDTO extends AdminUpdateSysTenantDTO {

    @ApiModelProperty(value = "管理员账号", required = true)
    @NotBlank(message = "管理员账号不能为空")
    private String tenantAdminUsername;

    @ApiModelProperty(value = "管理员密码", required = true)
    @NotBlank(message = "管理员密码不能为空")
    private String tenantAdminPassword;

    @ApiModelProperty(value = "管理员邮箱", required = true)
    @Pattern(message = "邮箱格式不正确", regexp = HelioConstant.Regex.EMAIL)
    @NotBlank(message = "管理员邮箱不能为空")
    private String tenantAdminEmail;

    @ApiModelProperty(value = "管理员手机号", required = true)
    @Pattern(message = "手机号格式不正确", regexp = HelioConstant.Regex.CHINA_MAINLAND_PHONE_NO)
    @NotBlank(message = "管理员手机号不能为空")
    private String tenantAdminPhoneNo;

}
