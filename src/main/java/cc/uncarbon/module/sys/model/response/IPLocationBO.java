package cc.uncarbon.module.sys.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * IP地址属地
 * 首用于系统日志
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IPLocationBO implements Serializable {

    @Schema(description = "国家或地区名")
    private String regionName;

    @Schema(description = "省级行政区名")
    private String provinceName;

    @Schema(description = "市级行政区名")
    private String cityName;

    @Schema(description = "县级行政区名")
    private String districtName;

    /**
     * 未知属地
     */
    public static IPLocationBO unknown() {
        return new IPLocationBO("未知", null, null, null);
    }

    /**
     * 内网地址
     */
    public static IPLocationBO intranet() {
        return new IPLocationBO("内网", null, null, null);
    }

    /**
     * 位于中国内地
     */
    public static IPLocationBO inChina() {
        return new IPLocationBO("中国", null, null, null);
    }
}
