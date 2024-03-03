package cc.uncarbon.module.sys.model.response;

import cc.uncarbon.framework.core.enums.GenderEnum;
import cc.uncarbon.module.sys.enums.SysUserStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 后台用户BO
 * 仅保留基本信息
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserBaseInfoBO implements Serializable {

    @Schema(description = "账号")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "状态")
    private SysUserStatusEnum status;

    @Schema(description = "性别")
    private GenderEnum gender;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phoneNo;

}
