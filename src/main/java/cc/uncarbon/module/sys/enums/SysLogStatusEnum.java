package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 系统日志状态枚举类
 * @author Uncarbon
 */
@AllArgsConstructor
@Getter
public enum SysLogStatusEnum implements HelioBaseEnum<Integer> {

    /**
     * 未执行
     */
    NON_EXECUTION(0, "未执行"),

    /**
     * 成功
     */
    SUCCESS(1, "成功"),
    ;

    @EnumValue
    private final Integer value;
    private final String label;

}
