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
 * 后台管理-新增/编辑数据字典
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateSysDataDictDTO implements Serializable {

    @Schema(description = "主键ID", hidden = true, title = "仅更新时使用")
    private Long id;

    @Schema(description = "驼峰式键名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 100, message = "【驼峰式键名】最长100位")
    @NotBlank(message = "驼峰式键名不能为空")
    private String camelCaseKey;

    @Schema(description = "下划线式键名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 100, message = "【下划线式键名】最长100位")
    @NotBlank(message = "下划线式键名不能为空")
    private String underCaseKey;

    @Schema(description = "帕斯卡式键名", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 100, message = "【帕斯卡式键名】最长100位")
    @NotBlank(message = "帕斯卡式键名不能为空")
    private String pascalCaseKey;

    @Schema(description = "数据值", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "【数据值】最长255位")
    @NotBlank(message = "数据值不能为空")
    private String value;

    @Schema(description = "描述", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max = 255, message = "【描述】最长255位")
    @NotBlank(message = "描述不能为空")
    private String description;

    @Schema(description = "单位")
    @Size(max = 30, message = "【单位】最长30位")
    private String unit;

    @Schema(description = "取值范围")
    @Size(max = 255, message = "【取值范围】最长255位")
    private String valueRange;

    @Schema(description = "别称键名")
    @Size(max = 100, message = "【别称键名】最长100位")
    private String aliasKey;

}
