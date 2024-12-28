package cc.uncarbon.module.bizcommon.model.request;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

/**
 * 批量修改状态公共类
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBatchUpdateStatusDTO<I extends Serializable, E extends HelioBaseEnum<?>> implements Serializable {

    @Schema(description = "主键IDs")
    @NotEmpty(message = "ID不能为空")
    private Collection<I> ids;

    @Schema(description = "新状态")
    @NotNull(message = "新状态不能为空")
    private E newStatus;

}
