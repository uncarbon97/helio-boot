package cc.uncarbon.module.sys.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 菜单类型枚举类
 * @author Uncarbon
 */
@AllArgsConstructor
@Getter
public enum SysMenuTypeEnum implements HelioBaseEnum<Integer> {

    /**
     * 可以认为是父级菜单
     */
    DIR(0, "目录"),

    /**
     * 可以认为是子菜单
     */
    MENU(1, "菜单"),

    /**
     * 可以认为是页内按钮
     */
    BUTTON(2, "按钮"),

    /**
     * 外链
     */
    EXTERNAL_LINK(3, "外链"),
    ;

    @EnumValue
    private final Integer value;
    private final String label;

}
