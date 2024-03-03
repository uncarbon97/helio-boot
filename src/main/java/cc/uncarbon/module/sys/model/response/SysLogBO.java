package cc.uncarbon.module.sys.model.response;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "创建时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "操作内容")
    private String operation;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "状态")
    private SysLogStatusEnum status;

    @Schema(description = "用户UA")
    private String userAgent;

    @Schema(description = "IP地址属地-国家或地区名")
    private String ipLocationRegionName;

    @Schema(description = "IP地址属地-省级行政区名")
    private String ipLocationProvinceName;

    @Schema(description = "IP地址属地-市级行政区名")
    private String ipLocationCityName;

    @Schema(description = "IP地址属地-县级行政区名")
    private String ipLocationDistrictName;

}
