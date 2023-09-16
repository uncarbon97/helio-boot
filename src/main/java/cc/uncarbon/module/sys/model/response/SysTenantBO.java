package cc.uncarbon.module.sys.model.response;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 系统租户BO
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysTenantBO implements Serializable {

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

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "租户名")
    private String tenantName;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "状态")
    private EnabledStatusEnum status;

    @ApiModelProperty(value = "租户管理员用户基本信息")
    private SysUserBaseInfoBO tenantAdminUser;

}
