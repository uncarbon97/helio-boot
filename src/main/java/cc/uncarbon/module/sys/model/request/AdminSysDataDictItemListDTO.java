package cc.uncarbon.module.sys.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


/**
 * 后台管理-分页列表数据字典项 DTO
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminSysDataDictItemListDTO implements Serializable {

    @Schema(description = "所属分类ID", hidden = true)
    private Long classifiedId;

}
