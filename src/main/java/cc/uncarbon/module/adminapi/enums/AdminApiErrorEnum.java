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

    /**
     * 很少遇到；但是如果出现了只会提示默认的「请稍后再试」，不方便排查，还是整个文案比较好
     */
    UPLOAD_FILE_NOT_EXIST(400, "欲上传的文件可能已被删除，请重新选择");

    private final Integer value;
    private final String label;

}
