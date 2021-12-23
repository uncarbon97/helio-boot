package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 通用状态枚举类
 *
 * @author Uncarbon
 */
@AllArgsConstructor
@Getter
public enum GenericStatusEnum implements HelioBaseEnum<Integer> {

    /**
     * 禁用
     */
    DISABLED(0, "禁用"),

    /**
     * 启用
     */
    ENABLED(1, "启用"),
    ;

    @EnumValue
    private final Integer value;
    private final String label;

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
