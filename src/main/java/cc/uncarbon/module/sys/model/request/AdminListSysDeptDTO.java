package cc.uncarbon.module.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 后台管理-分页列表部门
 * @author Uncarbon
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminListSysDeptDTO implements Serializable {

    @ApiModelProperty(value = "名称(关键词)")
    private String title;

    @ApiModelProperty(value = "上级ID(无上级节点设置为0)")
    private Long parentId;

}
