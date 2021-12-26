package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;


/**
 * 后台操作日志
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_log")
public class SysLogEntity extends HelioBaseEntity<Long> {

	@ApiModelProperty(value = "用户ID")
	@TableField(value = "user_id")
	private Long userId;

	@ApiModelProperty(value = "用户账号")
	@TableField(value = "username")
	private String username;

	@ApiModelProperty(value = "操作内容")
	@TableField(value = "operation")
	private String operation;

	@ApiModelProperty(value = "请求方法")
	@TableField(value = "method")
	private String method;

	@ApiModelProperty(value = "请求参数")
	@TableField(value = "params")
	private String params;

	@ApiModelProperty(value = "IP地址")
	@TableField(value = "ip")
	private String ip;

	@ApiModelProperty(value = "状态")
	@TableField(value = "status")
	private SysLogStatusEnum status;

}
