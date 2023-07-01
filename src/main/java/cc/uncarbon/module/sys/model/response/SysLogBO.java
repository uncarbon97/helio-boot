package cc.uncarbon.module.sys.model.response;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
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
 * 系统日志BO
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysLogBO implements Serializable {

    @ApiModelProperty(value = "创建时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "操作内容")
    private String operation;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "状态")
    private SysLogStatusEnum status;

    @ApiModelProperty(value = "用户UA")
    private String userAgent;

    @ApiModelProperty(value = "IP地址属地-国家或地区名")
    private String ipLocationRegionName;

    @ApiModelProperty(value = "IP地址属地-省级行政区名")
    private String ipLocationProvinceName;

    @ApiModelProperty(value = "IP地址属地-市级行政区名")
    private String ipLocationCityName;

    @ApiModelProperty(value = "IP地址属地-县级行政区名")
    private String ipLocationDistrictName;

}
