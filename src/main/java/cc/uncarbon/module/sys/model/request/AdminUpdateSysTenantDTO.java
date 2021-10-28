package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.module.sys.enums.GenericStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 后台管理-编辑系统租户
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminUpdateSysTenantDTO implements Serializable {

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "租户名", required = true)
    @NotBlank(message = "租户名不能为空")
    private String tenantName;

    @ApiModelProperty(value = "租户ID(纯数字)", required = true)
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "状态不能为空")
    private GenericStatusEnum status;

    @ApiModelProperty(value = "备注")
    private String remark;

}
