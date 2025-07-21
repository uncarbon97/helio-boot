package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.exception.BusinessException;
import cn.hutool.core.text.CharSequenceUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 后台管理-更新当前后台用户头像
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminUpdateCurrentSysUserAvatarDTO implements Serializable {


    @Schema(description = "头像URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "头像输入长度超限")
    private String avatarUrl;

    /**
     * 实际使用时，请手动补充安全检查机制（如检查头像URL所属的CDN域名）
     */
    public void securityCheck() {
        if (!CharSequenceUtil.startWithIgnoreCase(this.avatarUrl, "http")) {
            throw new BusinessException(400, "头像输入格式不正确");
        }
    }

}
