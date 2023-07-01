package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "操作内容")
    private String operation;

    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "状态")
    private SysLogStatusEnum status;

    @ApiModelProperty(value = "错误原因堆栈")
    private String errorStacktrace;

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
