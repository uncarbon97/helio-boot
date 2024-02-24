package cc.uncarbon.module.sys.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 后台管理-分页列表后台角色
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminListSysRoleDTO implements Serializable {

    @ApiModelProperty(value = "角色名(关键词)")
    private String title;

    @ApiModelProperty(value = "角色编码(关键词)")
    private String value;

}
