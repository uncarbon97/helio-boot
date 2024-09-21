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
 * 后台管理-新增/编辑数据字典项 DTO
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminSysDataDictItemInsertOrUpdateDTO implements Serializable {

    @Schema(description = "主键ID", hidden = true, title = "仅更新时使用")
    private Long id;

    @Schema(description = "所属分类ID", hidden = true)
    private Long classifiedId;

    @Schema(description = "字典项编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "【字典项编码】最长255位")
    @NotBlank(message = "字典项编码不能为空")
    private String code;

    @Schema(description = "字典项标签", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "【字典项标签】最长255位")
    @NotBlank(message = "字典项标签不能为空")
    private String label;

    @Schema(description = "字典项值", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 4096, message = "【字典项值】最长4096位")
    @NotBlank(message = "字典项值不能为空")
    private String value;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态不能为空")
    private EnabledStatusEnum status;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "描述")
    @Size(max = 255, message = "【描述】最长255位")
    private String description;

}
