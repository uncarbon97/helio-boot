package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 通用状态枚举类
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

}
