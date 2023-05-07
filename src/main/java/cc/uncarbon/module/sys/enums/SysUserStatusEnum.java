package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 预置系统后台用户状态枚举类
 */
@AllArgsConstructor
@Getter
public enum SysUserStatusEnum implements HelioBaseEnum<Integer> {

    BANNED(0, "封禁"),

    ENABLED(1, "正常"),

    ;
    @EnumValue
    private final Integer value;
    private final String label;

}
