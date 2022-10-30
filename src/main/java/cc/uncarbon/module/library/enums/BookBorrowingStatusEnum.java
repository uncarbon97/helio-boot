package cc.uncarbon.module.library.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 书籍借阅状态枚举
 *
 * @author Uncarbon
 */
@AllArgsConstructor
@Getter
public enum BookBorrowingStatusEnum implements HelioBaseEnum<Integer> {

    UNKNOWN(0, "未知"),

    BORROWING(1, "借阅中"),

    RETURNED(2, "已归还"),

    OVERDUE(3, "已逾期"),

    ;

    @EnumValue
    private final Integer value;
    private final String label;

}
