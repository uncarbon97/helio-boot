package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 后台管理-新增系统日志
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertSysLogDTO implements Serializable {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户账号")
    private String username;

    @Schema(description = "操作内容")
    private String operation;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "请求参数")
    private String params;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "状态")
    private SysLogStatusEnum status;

    @Schema(description = "错误原因堆栈")
    private String errorStacktrace;

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
