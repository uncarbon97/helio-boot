package cc.uncarbon.module.library.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 更新书籍存量事件类型枚举
 *
 * @author Uncarbon
 */
@AllArgsConstructor
@Getter
public enum UpdateBookQuantityEventTypeEnum implements HelioBaseEnum<Integer> {

    UPDATE_BORROWING_QUANTITY(1, "更新被借阅数量"),

    UPDATE_DAMAGED_QUANTITY(2, "更新被损坏数量"),
    ;

    @EnumValue
    private final Integer value;
    private final String label;

}
