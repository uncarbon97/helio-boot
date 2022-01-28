package cc.uncarbon.module.sys.model.response;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
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
 * 后台操作日志BO
 * @author Uncarbon
 */
@ApiModel(value = "后台操作日志")
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysLogBO implements Serializable {

    @ApiModelProperty(value = "创建时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT, timezone = HelioConstant.Jackson.TIME_ZONE)
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "操作内容")
    private String operation;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "状态")
    private SysLogStatusEnum status;

}
