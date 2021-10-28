package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.sys.enums.GenderEnum;
import cc.uncarbon.module.sys.enums.SysUserStatusEnum;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


/**
 * 后台用户
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_user")
public class SysUserEntity extends HelioBaseEntity<Long> {

	@ApiModelProperty(value = "账号")
	@Alias(value = "username")
	@TableField(value = "pin")
	private String pin;

	@ApiModelProperty(value = "密码")
	@TableField(value = "pwd")
	private String pwd;

	@ApiModelProperty(value = "盐")
	@TableField(value = "salt")
	private String salt;

	@ApiModelProperty(value = "昵称")
	@TableField(value = "nickname")
	private String nickname;

	@ApiModelProperty(value = "状态(0=正常 -1=封禁)")
	@TableField(value = "status")
	private SysUserStatusEnum status;

	@ApiModelProperty(value = "性别")
	@TableField(value = "gender")
	private GenderEnum gender;

	@ApiModelProperty(value = "邮箱")
	@TableField(value = "email")
	private String email;

	@ApiModelProperty(value = "手机号")
	@TableField(value = "phone_no")
	private String phoneNo;

	@ApiModelProperty(value = "最后登录时刻")
	@TableField(value = "last_login_at")
	private LocalDateTime lastLoginAt;

}
