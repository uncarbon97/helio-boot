package cc.uncarbon.module.library.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import cc.uncarbon.module.sys.enums.GenderEnum;
import cc.uncarbon.module.sys.enums.GenericStatusEnum;
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
 * 会员
 *
 * @author Uncarbon
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "tb_member")
public class MemberEntity extends HelioBaseEntity<Long> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "学号/工号")
    @TableField(value = "username")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    @TableField(value = "real_name")
    private String realName;

    @ApiModelProperty(value = "状态")
    @TableField(value = "status")
    private GenericStatusEnum status;

    @ApiModelProperty(value = "性别")
    @TableField(value = "gender")
    private GenderEnum gender;

    @ApiModelProperty(value = "手机号")
    @TableField(value = "phone_no")
    private String phoneNo;

}
