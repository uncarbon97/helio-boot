package cc.uncarbon.module.oss.model.response;

import cc.uncarbon.framework.core.constant.HelioConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 上传文件信息 BO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OssFileInfoBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "创建时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时刻")
    @DateTimeFormat(pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = HelioConstant.Jackson.DATE_TIME_FORMAT)
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "存储平台")
    private String storagePlatform;

    @ApiModelProperty(value = "基础存储路径")
    private String storageBasePath;

    @ApiModelProperty(value = "存储路径")
    private String storagePath;

    @ApiModelProperty(value = "存储文件名")
    private String storageFilename;

    @ApiModelProperty(value = "原始文件名")
    private String originalFilename;

    @ApiModelProperty(value = "扩展名")
    private String extendName;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "MD5")
    private String md5;

    @ApiModelProperty(value = "类别编号")
    private String classified;

    @ApiModelProperty(value = "对象存储直链")
    private String directUrl;

    /**
     * 取完整的存储文件名（带扩展名）
     */
    public String getStorageFilenameFull() {
        return String.format("%s.%s", this.getStorageFilename(), this.getExtendName());
    }

    /**
     * 取完整的原始文件名（带扩展名）
     */
    public String getOriginalFilenameFull() {
        return String.format("%s.%s", this.getOriginalFilename(), this.getExtendName());
    }

}
