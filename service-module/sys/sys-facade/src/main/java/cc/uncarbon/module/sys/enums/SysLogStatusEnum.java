package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 预置系统日志状态枚举类
 */
@AllArgsConstructor
@Getter
public enum SysLogStatusEnum implements HelioBaseEnum<Integer> {

    NON_EXECUTION(0, "未执行"),

    SUCCESS(1, "成功"),

    FAILED(2, "失败"),

    ;
    @EnumValue
    private final Integer value;
    private final String label;

}
