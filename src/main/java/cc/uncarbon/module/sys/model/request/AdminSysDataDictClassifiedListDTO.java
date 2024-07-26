package cc.uncarbon.module.sys.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;


/**
 * 后台管理-分页列表数据字典分类 DTO
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminSysDataDictClassifiedListDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @Schema(description = "分类编码")
    private String code;

}
