package cc.uncarbon.module.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 后台管理-新增/编辑系统参数
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateSysParamDTO implements Serializable {

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "键名", required = true)
    @Size(max = 50, message = "【键名】最长50位")
    @NotBlank(message = "键名不能为空")
    private String name;

    @ApiModelProperty(value = "键值", required = true)
    @Size(max = 255, message = "【键值】最长255位")
    @NotBlank(message = "键值不能为空")
    private String value;

    @ApiModelProperty(value = "描述", required = true)
    @Size(max = 255, message = "【描述】最长255位")
    @NotBlank(message = "描述不能为空")
    private String description;

}
