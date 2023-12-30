package cc.uncarbon.module.oss.model.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


/**
 * 下载文件结果 BO
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OssFileDownloadReplyBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "是否直接重定向到对象存储直链", notes = "如果允许客户端直接从“对象存储直链”下载，则本字段可以置 true")
    private boolean redirect2DirectUrl;

    @ApiModelProperty(value = "文件数据", notes = "如果允许客户端直接从“对象存储直链”下载，则本字段可以置空")
    private byte[] fileBytes;

    @ApiModelProperty(value = "对象存储直链")
    private String directUrl;

    @ApiModelProperty(value = "存储文件名")
    private String storageFilename;

}
