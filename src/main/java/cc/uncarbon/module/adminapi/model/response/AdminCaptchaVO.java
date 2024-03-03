package cc.uncarbon.module.adminapi.model.response;

import cc.uncarbon.module.adminapi.model.interior.AdminCaptchaContainer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 后台管理-验证码 VO
 */
@Getter
public class AdminCaptchaVO {

    @Schema(description = "验证码图片Base64")
    private final String captchaImage;

    @Schema(description = "验证码唯一标识")
    private final String captchaId;

    @Schema(description = "验证码失效时刻")
    private final LocalDateTime expiredAt;


    public AdminCaptchaVO(AdminCaptchaContainer source) {
        this.captchaImage = source.getImage().getImageBase64Data();
        this.captchaId = source.getUuid();
        this.expiredAt = source.getExpiredAt();
    }
}
