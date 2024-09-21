package cc.uncarbon.module.oss.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 上传文件属性 DTO
 */
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadFileAttributeDTO implements Serializable {

    @Schema(description = "文件类别", example = "id_card=身份证 driver_license=驾驶证")
    private String classified;

    /*
    以下字段为内部使用
     */
    @Schema(description = "原始文件名", hidden = true)
    private String originalFilename;

    @Schema(description = "MIME类型", hidden = true)
    private String contentType;

    @Schema(description = "MD5", hidden = true)
    private String md5;

    @Schema(description = "指定要上传到的平台名（null则取值默认平台）", hidden = true)
    private String platform;

    @Schema(description = "从对象存储服务器中下载时，以原始文件名命名（通过指定 metadata 实现）", hidden = true)
    private boolean useOriginalFilenameAsDownloadFileName;

}
