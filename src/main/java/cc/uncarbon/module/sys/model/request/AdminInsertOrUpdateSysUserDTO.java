package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.enums.GenderEnum;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.module.sys.enums.SysUserStatusEnum;
import cn.hutool.core.text.CharSequenceUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;


/**
 * 后台管理-新增/编辑后台用户
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateSysUserDTO implements Serializable {

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "所属租户ID", hidden = true, notes = "仅新增时使用")
    private Long tenantId;

    @ApiModelProperty(value = "账号", required = true)
    @Size(min = 6, max = 16, message = "【账号】最短6位，最长16位")
    @NotBlank(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "密码字符串(仅注册时有效)")
    private String passwordOfNewUser;

    @ApiModelProperty(value = "昵称", required = true)
    @Size(max = 100, message = "【昵称】最长100位")
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "状态不能为空")
    private SysUserStatusEnum status;

    @ApiModelProperty(value = "性别", required = true)
    @NotNull(message = "性别不能为空")
    private GenderEnum gender;

    @ApiModelProperty(value = "邮箱", required = true)
    @Pattern(message = "邮箱格式有误", regexp = HelioConstant.Regex.EMAIL)
    @Size(max = 255, message = "【邮箱】最长255位")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @ApiModelProperty(value = "手机号", required = true)
    @Pattern(message = "手机号格式有误", regexp = HelioConstant.Regex.CHINA_MAINLAND_PHONE_NO)
    @Size(max = 20, message = "【手机号】最长20位")
    @NotBlank(message = "手机号不能为空")
    private String phoneNo;

    @ApiModelProperty(value = "所属部门ID")
    private Long deptId;


    public void validate() {
        boolean isUpdate = Objects.nonNull(id);
        if (!isUpdate) {
            // 新增
            int passwordOfNewUserLen = CharSequenceUtil.length(passwordOfNewUser);
            if (passwordOfNewUserLen < 8 || passwordOfNewUserLen > 20) {
                throw new BusinessException("【密码】最短8位，最长20位");
            }
        }
    }

}
