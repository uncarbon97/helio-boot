package cc.uncarbon.module.library.model.request;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.framework.core.enums.GenderEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 后台管理-新增/编辑会员 DTO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateMemberDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "学号/工号", required = true)
    @NotBlank(message = "学号/工号不能为空")
    private String username;

    @ApiModelProperty(value = "真实姓名", required = true)
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "状态不能为空")
    private EnabledStatusEnum status;

    @ApiModelProperty(value = "性别", required = true)
    @NotNull(message = "性别不能为空")
    private GenderEnum gender;

    @ApiModelProperty(value = "手机号")
    private String phoneNo;

}
