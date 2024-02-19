package cc.uncarbon.module.adminapi.model.interior;

import cn.hutool.captcha.AbstractCaptcha;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 后台管理-验证码容器
 */
@RequiredArgsConstructor
@Getter
public class AdminCaptchaContainer {

    /**
     * 验证码图片对象
     */
    private final AbstractCaptcha image;

    /**
     * 验证码唯一标识（UUID）
     */
    private final String uuid;

    /**
     * 验证码失效时刻
     */
    private final LocalDateTime expiredAt;

}
