package cc.uncarbon.module.sys.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Schema(description = "主键ID", hidden = true, title = "仅更新时使用")
    private Long id;

    @Schema(description = "键名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 50, message = "【键名】最长50位")
    @NotBlank(message = "键名不能为空")
    private String name;

    @Schema(description = "键值", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "【键值】最长255位")
    @NotBlank(message = "键值不能为空")
    private String value;

    @Schema(description = "描述", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "【描述】最长255位")
    @NotBlank(message = "描述不能为空")
    private String description;

}
