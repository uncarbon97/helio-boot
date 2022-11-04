package cc.uncarbon.module.oss.facade;

import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.module.oss.model.request.UploadFileAttributeDTO;
import cc.uncarbon.module.oss.model.response.OssFileDownloadReplyBO;
import cc.uncarbon.module.oss.model.response.OssFileInfoBO;
import lombok.NonNull;

/**
 * 文件上传下载门面
 *
 * @author Uncarbon
 */
public interface OssUploadDownloadFacade {

    /**
     * 根据哈希值，查找是否已有文件
     */
    OssFileInfoBO findByHash(String md5);

    /**
     * 正常上传文件到服务端
     *
     * @param fileBytes 文件数据
     * @param attr      附加属性
     */
    OssFileInfoBO upload(byte[] fileBytes, @NonNull UploadFileAttributeDTO attr) throws BusinessException;

    /**
     * 根据文件ID下载
     *
     * @throws BusinessException 业务异常，如：文件ID无效；原始文件不存在
     */
    OssFileDownloadReplyBO downloadById(Long fileInfoId) throws BusinessException;

    /**
     * 是否为本地存储平台
     * @param storagePlatform 存储平台名
     */
    boolean isLocalPlatform(String storagePlatform);

}
