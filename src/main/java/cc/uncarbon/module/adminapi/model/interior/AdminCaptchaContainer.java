package cc.uncarbon.module.adminapi.model.interior;

import cn.hutool.captcha.AbstractCaptcha;

import java.time.LocalDateTime;

/**
 * 后台管理-验证码容器
 *
 * @param image     验证码图片对象
 * @param uuid      验证码唯一标识（UUID）
 * @param expiredAt 验证码失效时刻
 */
public record AdminCaptchaContainer(AbstractCaptcha image, String uuid, LocalDateTime expiredAt) {
}
