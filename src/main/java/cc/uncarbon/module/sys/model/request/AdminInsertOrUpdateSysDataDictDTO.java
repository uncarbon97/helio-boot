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
 * 后台管理-新增/编辑数据字典
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateSysDataDictDTO implements Serializable {

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "驼峰式键名", required = true)
    @Size(max = 100, message = "【驼峰式键名】最长100位")
    @NotBlank(message = "驼峰式键名不能为空")
    private String camelCaseKey;

    @ApiModelProperty(value = "下划线式键名", required = true)
    @Size(max = 100, message = "【下划线式键名】最长100位")
    @NotBlank(message = "下划线式键名不能为空")
    private String underCaseKey;

    @ApiModelProperty(value = "帕斯卡式键名", required = true)
    @Size(max = 100, message = "【帕斯卡式键名】最长100位")
    @NotBlank(message = "帕斯卡式键名不能为空")
    private String pascalCaseKey;

    @ApiModelProperty(value = "数据值", required = true)
    @Size(max = 255, message = "【数据值】最长255位")
    @NotBlank(message = "数据值不能为空")
    private String value;

    @ApiModelProperty(value = "描述", required = true)
    @Size(max = 255, message = "【描述】最长255位")
    @NotBlank(message = "描述不能为空")
    private String description;

    @ApiModelProperty(value = "单位")
    @Size(max = 30, message = "【单位】最长30位")
    private String unit;

    @ApiModelProperty(value = "取值范围")
    @Size(max = 255, message = "【取值范围】最长255位")
    private String valueRange;

    @ApiModelProperty(value = "别称键名")
    @Size(max = 100, message = "【别称键名】最长100位")
    private String aliasKey;

}
