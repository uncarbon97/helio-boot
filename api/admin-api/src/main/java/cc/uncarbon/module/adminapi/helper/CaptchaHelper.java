package cc.uncarbon.module.adminapi.helper;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
     * 生成一个验证码图片对象
     *
     * @param uuid UUID
     * @return ShearCaptcha
     */
    public ShearCaptcha generate(String uuid) {
        // 定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(196, 50, 4, 4);

        // 将验证码答案保存至 redis , 有效期5分钟
        stringRedisTemplate.opsForValue().set(String.format(CACHE_KEY_CAPTCHA_ANSWER, uuid), captcha.getCode(), 300, TimeUnit.SECONDS);

        return captcha;
    }

    /**
     * 校验验证码是否输入正确
     *
     * @param uuid          UUID
     * @param captchaAnswer 验证码答案
     * @param removeWhenEquals 匹配时自动移除缓存键
     * @return 是否正确
     */
    public boolean validate(String uuid, String captchaAnswer, boolean removeWhenEquals) {
        if (StrUtil.hasBlank(uuid, captchaAnswer)) {
            return false;
        }

        String cacheKey = String.format(CACHE_KEY_CAPTCHA_ANSWER, uuid);
        String answerInRedis = stringRedisTemplate.opsForValue().get(cacheKey);
        boolean equals = StrUtil.equalsIgnoreCase(answerInRedis, captchaAnswer);
        if (equals && removeWhenEquals) {
            stringRedisTemplate.delete(cacheKey);
        }

        return equals;
    }
}
