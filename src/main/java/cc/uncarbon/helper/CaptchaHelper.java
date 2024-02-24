package cc.uncarbon.helper;

import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.module.adminapi.enums.AdminApiErrorEnum;
import cc.uncarbon.module.adminapi.model.interior.AdminCaptchaContainer;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * 验证码助手类；可将验证码答案缓存至 Redis
 *
 * @author Uncarbon
 */
@Component
@RequiredArgsConstructor
public class CaptchaHelper {

    private final RedisTemplate<String, String> stringRedisTemplate;

    private static final String CACHE_KEY_CAPTCHA_ANSWER = "Authorization:captcha:uuid_%s";

    /**
     * 验证码答案长度
     */
    private static final int CAPTCHA_ANSWER_LENGTH = 4;

    /**
     * 生成一个验证码
     */
    public AdminCaptchaContainer generate() {
        // redis预占位；随机10个UUID，应该有个能成的吧……
        UUID uuid = UUID.randomUUID();
        String captchaCacheKey = null;
        Boolean stubFlag = Boolean.FALSE;
        for (int count = 0; count < 10; count++) {
            captchaCacheKey = String.format(CACHE_KEY_CAPTCHA_ANSWER, uuid.toString(true));
            stubFlag = stringRedisTemplate.opsForValue().setIfAbsent(captchaCacheKey, CharSequenceUtil.EMPTY);
            if (Boolean.TRUE.equals(stubFlag)) {
                break;
            }
            uuid = UUID.randomUUID();
        }
        if (!Boolean.TRUE.equals(stubFlag)) {
            throw new BusinessException(AdminApiErrorEnum.CAPTCHA_GENERATE_FAILED);
        }

        // 定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(196, 50, CAPTCHA_ANSWER_LENGTH, 4);

        // 将验证码答案保存至 redis, 有效期5分钟
        stringRedisTemplate.opsForValue().set(captchaCacheKey, captcha.getCode(), 300, TimeUnit.SECONDS);
        LocalDateTime expiredAt = LocalDateTimeUtil.offset(LocalDateTimeUtil.now(), 300, ChronoUnit.SECONDS);

        return new AdminCaptchaContainer(captcha, uuid.toString(true), expiredAt);
    }

    /**
     * 核验验证码是否输入正确
     *
     * @param uuid             验证码唯一标识（UUID）
     * @param captchaAnswer    验证码答案
     * @return 是否正确
     */
    public boolean validate(String uuid, String captchaAnswer) {
        if (CharSequenceUtil.hasBlank(uuid, captchaAnswer)) {
            return false;
        }

        String cacheKey = String.format(CACHE_KEY_CAPTCHA_ANSWER, uuid);
        boolean equals;
        try {
            if (CharSequenceUtil.length(captchaAnswer) != CAPTCHA_ANSWER_LENGTH) {
                // 长度不同
                return false;
            }

            String answerInRedis = stringRedisTemplate.opsForValue().get(cacheKey);
            equals = CharSequenceUtil.equalsIgnoreCase(answerInRedis, captchaAnswer);
        } finally {
            stringRedisTemplate.delete(cacheKey);
        }
        return equals;
    }
}
