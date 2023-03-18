package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 通用状态枚举类
 */
@AllArgsConstructor
@Getter
public enum GenericStatusEnum implements HelioBaseEnum<Integer> {

    DISABLED(0, "禁用"),
    ENABLED(1, "启用"),

    ;
    @EnumValue
    private final Integer value;
    private final String label;

    /**
     * 根据值得到枚举类对象
     * @param value 外部值
     * @return null or 枚举类对象
     */
    public static GenericStatusEnum of(Integer value) {
        if (value == null) {
            return null;
        }
        // 区分度小，直接用if判断了
        if (DISABLED.value.equals(value)) {
            return DISABLED;
        }
        if (ENABLED.value.equals(value)) {
            return ENABLED;
        }
        return null;
    }

    /**
     * 取反值
     * @param old 原值
     * @return 新值
     */
    public static GenericStatusEnum reverse(GenericStatusEnum old) {
        if (old == null) {
            return null;
        }

        switch (old) {
            case DISABLED:
                return GenericStatusEnum.ENABLED;
            case ENABLED:
                return GenericStatusEnum.DISABLED;
        }

        throw new IllegalArgumentException();
    }
}
