package cc.uncarbon.module.adminapi.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * admin-api模块错误枚举类
 */
@AllArgsConstructor
@Getter
public enum AdminApiErrorEnum implements HelioBaseEnum<Integer> {

    CAPTCHA_GENERATE_FAILED(500, "验证码生成失败，请稍后再试"),

    CAPTCHA_VALIDATE_FAILED(400, "验证码不正确，请重新输入"),

    ;
    private final Integer value;
    private final String label;

}
