package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.enums.GenderEnum;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.module.sys.enums.SysUserStatusEnum;
import cn.hutool.core.text.CharSequenceUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Schema(description = "主键ID", hidden = true, title = "仅更新时使用")
    private Long id;

    @Schema(description = "所属租户ID", hidden = true, title = "仅新增时使用")
    private Long tenantId;

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 6, max = 16, message = "【账号】最短6位，最长16位")
    @NotBlank(message = "账号不能为空")
    private String username;

    @Schema(description = "密码字符串(仅注册时有效)")
    private String passwordOfNewUser;

    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 100, message = "【昵称】最长100位")
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    private SysUserStatusEnum status;

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

    @Schema(description = "所属部门ID")
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
