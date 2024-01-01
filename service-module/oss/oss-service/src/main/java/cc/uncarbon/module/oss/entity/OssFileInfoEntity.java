package cc.uncarbon.module.oss.entity;

import cc.uncarbon.framework.crud.entity.HelioBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;


/**
 * 上传文件信息
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "oss_file_info")
public class OssFileInfoEntity extends HelioBaseEntity<Long> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "存储平台")
    @TableField(value = "storage_platform")
    private String storagePlatform;

    @ApiModelProperty(value = "基础存储路径")
    @TableField(value = "storage_base_path")
    private String storageBasePath;

    @ApiModelProperty(value = "存储路径")
    @TableField(value = "storage_path")
    private String storagePath;

    @ApiModelProperty(value = "存储文件名")
    @TableField(value = "storage_filename")
    private String storageFilename;

    @ApiModelProperty(value = "原始文件名")
    @TableField(value = "original_filename")
    private String originalFilename;

    @ApiModelProperty(value = "扩展名")
    @TableField(value = "extend_name")
    private String extendName;

    @ApiModelProperty(value = "文件大小")
    @TableField(value = "file_size")
    private Long fileSize;

    @ApiModelProperty(value = "MD5")
    @TableField(value = "md5")
    private String md5;

    @ApiModelProperty(value = "类别名")
    @TableField(value = "classified")
    private String classified;

    @ApiModelProperty(value = "对象存储直链")
    @TableField(value = "direct_url")
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
