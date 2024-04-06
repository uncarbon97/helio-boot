package cc.uncarbon.module.oss.model.request;

import cc.uncarbon.framework.core.constant.HelioConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 后台管理-分页列表上传文件信息 DTO
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminListOssFileInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @Schema(description = "原始文件名(关键词)")
    private String originalFilename;

    @Schema(description = "扩展名")
    private String extendName;

    @Schema(description = "文件类别")
    private String classified;

    @Schema(description = "时间区间起")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime beginAt;

    @Schema(description = "时间区间止")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime endAt;

}
