package cc.uncarbon.module.library.model.response;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.framework.core.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 会员 BO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "创建时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "学号/工号")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "状态")
    private EnabledStatusEnum status;

    @ApiModelProperty(value = "性别")
    private GenderEnum gender;

    @ApiModelProperty(value = "手机号")
    private String phoneNo;

}
