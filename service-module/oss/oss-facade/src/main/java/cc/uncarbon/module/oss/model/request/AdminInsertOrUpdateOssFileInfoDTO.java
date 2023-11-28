package cc.uncarbon.module.oss.model.request;

import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 后台管理-新增/编辑上传文件信息 DTO
 *
 * @author Uncarbon
 */
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminInsertOrUpdateOssFileInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", hidden = true, notes = "仅更新时使用")
    private Long id;

    @ApiModelProperty(value = "前置路径", required = true)
    @NotBlank(message = "前置路径不能为空")
    private String path;

    @ApiModelProperty(value = "文件名", required = true)
    @NotBlank(message = "文件名不能为空")
    private String filename;

    @ApiModelProperty(value = "扩展名", required = true)
    @NotBlank(message = "扩展名不能为空")
    private String extendName;

    @ApiModelProperty(value = "文件大小", required = true)
    @NotNull(message = "文件大小不能为空")
    private Long filesize;

    @ApiModelProperty(value = "MD5", required = true)
    @NotBlank(message = "MD5不能为空")
    private String md5;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "状态不能为空")
    private EnabledStatusEnum status;

    @ApiModelProperty(value = "类别编号")
    private Integer classified;

}
