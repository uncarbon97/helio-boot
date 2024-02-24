package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 后台管理-编辑系统租户
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
    @Size(max = 50, message = "【租户名】最长50位")
    @NotBlank(message = "租户名不能为空")
    private String tenantName;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "状态不能为空")
    private EnabledStatusEnum status;

    @ApiModelProperty(value = "备注")
    @Size(max = 255, message = "【备注】最长255位")
    private String remark;

}
