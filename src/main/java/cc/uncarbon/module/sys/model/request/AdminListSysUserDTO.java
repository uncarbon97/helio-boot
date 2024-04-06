package cc.uncarbon.module.sys.model.request;

import cc.uncarbon.module.sys.constant.SysConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;


/**
 * 后台管理-分页列表后台用户
 */
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminListSysUserDTO implements Serializable {

    @Schema(description = "手机号(关键词)")
    private String phoneNo;

    @Schema(description = "手动选择的部门ID")
    private Long selectedDeptId;

    /**
     * 是否需要根据【手动选择的部门】筛选用户
     */
    public boolean needFilterBySelectedDeptId() {
        return Objects.nonNull(selectedDeptId) && selectedDeptId > SysConstant.ROOT_PARENT_ID;
    }

}
