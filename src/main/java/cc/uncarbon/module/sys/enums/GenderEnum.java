package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 生理性别枚举类
 */
@AllArgsConstructor
@Getter
public enum GenderEnum implements HelioBaseEnum<Integer> {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女"),

    ;
    @EnumValue
    private final Integer value;
    private final String label;

}
