package cc.uncarbon.module.sys.entity;

import cc.uncarbon.framework.core.enums.GenderEnum;
import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.sys.enums.SysUserStatusEnum;
import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


/**
 * 后台用户
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "sys_user")
public class SysUserEntity extends HelioBaseEntity<Long> {

	@Schema(description = "账号")
	@Alias(value = "username")
	@TableField(value = "pin")
	private String pin;

	@Schema(description = "密码")
	@TableField(value = "pwd")
	private String pwd;

	@Schema(description = "盐")
	@TableField(value = "salt")
	private String salt;

	@Schema(description = "昵称")
	@TableField(value = "nickname")
	private String nickname;

	@Schema(description = "状态")
	@TableField(value = "status")
	private SysUserStatusEnum status;

	@Schema(description = "性别")
	@TableField(value = "gender")
	private GenderEnum gender;

	@Schema(description = "邮箱")
	@TableField(value = "email")
	private String email;

	@Schema(description = "手机号")
	@TableField(value = "phone_no")
	private String phoneNo;

	@Schema(description = "最后登录时刻")
	@TableField(value = "last_login_at")
	private LocalDateTime lastLoginAt;

}
