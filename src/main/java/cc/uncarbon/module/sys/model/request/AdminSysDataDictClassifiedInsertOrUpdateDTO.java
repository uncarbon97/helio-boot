package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


/**
 * 后台管理-新增/编辑数据字典分类 DTO
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminSysDataDictClassifiedInsertOrUpdateDTO implements Serializable {

    @Schema(description = "主键ID", hidden = true, title = "仅更新时使用")
    private Long id;

    @Schema(description = "分类编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "【分类编码】最长255位")
    @NotBlank(message = "分类编码不能为空")
    private String code;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "【分类名称】最长255位")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    private EnabledStatusEnum status;

    @Schema(description = "分类描述")
    @Size(max = 255, message = "【分类描述】最长255位")
    private String description;

}
