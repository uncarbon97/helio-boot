package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.module.sys.enums.GenericStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 后台管理-新增/编辑部门
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateSysDeptDTO implements Serializable {

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "所属租户ID", hidden = true, notes = "仅新增时使用")
    private Long tenantId;

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String title;

    @ApiModelProperty(value = "上级ID(无上级节点设置为0)")
    private Long parentId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态")
    private GenericStatusEnum status;

}
