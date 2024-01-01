package cc.uncarbon.module.oss.model.request;

import io.swagger.annotations.ApiModelProperty;
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

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件类别", example = "id_card=身份证 driver_license=驾驶证")
    private String classified;

    /*
    以下字段为内部使用
     */
    @ApiModelProperty(value = "原始文件名", hidden = true)
    private String originalFilename;

    @ApiModelProperty(value = "MIME类型", hidden = true)
    private String contentType;

    @ApiModelProperty(value = "MD5", hidden = true)
    private String md5;

}
