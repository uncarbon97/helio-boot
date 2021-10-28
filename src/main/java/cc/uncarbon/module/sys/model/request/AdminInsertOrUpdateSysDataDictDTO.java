package cc.uncarbon.module.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 后台管理-新增/编辑数据字典
 *
 * @author Uncarbon
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
    @NotBlank(message = "驼峰式键名不能为空")
    private String camelCaseKey;

    @ApiModelProperty(value = "下划线式键名", required = true)
    @NotBlank(message = "下划线式键名不能为空")
    private String underCaseKey;

    @ApiModelProperty(value = "帕斯卡式键名", required = true)
    @NotBlank(message = "帕斯卡式键名不能为空")
    private String pascalCaseKey;

    @ApiModelProperty(value = "键值", required = true)
    @NotBlank(message = "键值不能为空")
    private String value;

    @ApiModelProperty(value = "参数描述", required = true)
    @NotBlank(message = "参数描述不能为空")
    private String description;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "取值范围")
    private String valueRange;

    @ApiModelProperty(value = "别称键名")
    private String aliasKey;

}
