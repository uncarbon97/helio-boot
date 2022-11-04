package cc.uncarbon.module.library.model.request;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.module.library.enums.BookBorrowingQueryPeriodTypeEnum;
import cc.uncarbon.module.library.enums.BookBorrowingStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 后台管理-分页列表书籍借阅记录 DTO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminListBookBorrowingDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "会员学号/工号(关键词)")
    private String memberUsername;

    @ApiModelProperty(value = "会员真实姓名(关键词)")
    private String memberRealName;

    @ApiModelProperty(value = "书籍ID")
    private Long bookId;

    @ApiModelProperty(value = "状态")
    private BookBorrowingStatusEnum status;

    @ApiModelProperty(value = "时间区间类型")
    private BookBorrowingQueryPeriodTypeEnum queryPeriodType;

    @ApiModelProperty(value = "时间区间起")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime beginAt;

    @ApiModelProperty(value = "时间区间止")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime endAt;

}
