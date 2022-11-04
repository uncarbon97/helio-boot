package cc.uncarbon.module.library.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 书籍借阅查询条件，时间类型枚举
 *
 * @author Uncarbon
 */
@AllArgsConstructor
@Getter
public enum BookBorrowingQueryPeriodTypeEnum implements HelioBaseEnum<Integer> {

    BORROW(1, "借阅时间"),

    APPOINTED_RETURN(2, "约定归还时间"),

    ACTUAL_RETURN(3, "实际归还时间"),
    ;

    @EnumValue
    private final Integer value;
    private final String label;

}
