package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.module.sys.enums.GenericStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 后台管理-分页列表系统租户
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminListSysTenantDTO implements Serializable {

    @ApiModelProperty(value = "租户名(关键词)")
    private String tenantName;

    @ApiModelProperty(value = "租户ID(纯数字)")
    private Long tenantId;

    @ApiModelProperty(value = "状态")
    private GenericStatusEnum status;

    @ApiModelProperty(value = "时间区间起")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT, timezone = HelioConstant.Jackson.TIME_ZONE)
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime beginAt;

    @ApiModelProperty(value = "时间区间止")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT, timezone = HelioConstant.Jackson.TIME_ZONE)
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime endAt;

}
