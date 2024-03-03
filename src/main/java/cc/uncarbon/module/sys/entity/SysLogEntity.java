package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;


/**
 * 系统日志
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_log")
public class SysLogEntity extends HelioBaseEntity<Long> {

	@Schema(description = "用户ID")
	@TableField(value = "user_id")
	private Long userId;

	@Schema(description = "用户账号")
	@TableField(value = "username")
	private String username;

	@Schema(description = "操作内容")
	@TableField(value = "operation")
	private String operation;

	@Schema(description = "请求方法")
	@TableField(value = "method")
	private String method;

	@Schema(description = "请求参数")
	@TableField(value = "params")
	private String params;

	@Schema(description = "IP地址")
	@TableField(value = "ip")
	private String ip;

	@Schema(description = "状态")
	@TableField(value = "status")
	private SysLogStatusEnum status;

	@Schema(description = "错误原因堆栈")
	@TableField(value = "error_stacktrace")
	private String errorStacktrace;

	@Schema(description = "用户UA")
	@TableField(value = "user_agent")
	private String userAgent;

	@Schema(description = "IP地址属地-国家或地区名")
	@TableField(value = "ip_location_region_name")
	private String ipLocationRegionName;

	@Schema(description = "IP地址属地-省级行政区名")
	@TableField(value = "ip_location_province_name")
	private String ipLocationProvinceName;

	@Schema(description = "IP地址属地-市级行政区名")
	@TableField(value = "ip_location_city_name")
	private String ipLocationCityName;

	@Schema(description = "IP地址属地-县级行政区名")
	@TableField(value = "ip_location_district_name")
	private String ipLocationDistrictName;

}
