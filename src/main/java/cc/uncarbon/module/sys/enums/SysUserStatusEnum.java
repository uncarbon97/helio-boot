package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 后台用户状态枚举类
 * @author Uncarbon
 */
@AllArgsConstructor
@Getter
public enum SysUserStatusEnum implements HelioBaseEnum<Integer> {

    /**
     * 封禁
     */
    BANNED(0, "封禁"),

    /**
     * 正常
     */
    ENABLED(1, "正常"),
    ;

    @EnumValue
    private final Integer value;
    private final String label;

}
