package cc.uncarbon.module.oss.facade.impl;

import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.module.oss.enums.OssErrorEnum;
import cc.uncarbon.module.oss.facade.OssUploadDownloadFacade;
import cc.uncarbon.module.oss.model.request.UploadFileAttributeDTO;
import cc.uncarbon.module.oss.model.response.OssFileDownloadReplyBO;
import cc.uncarbon.module.oss.model.response.OssFileInfoBO;
import cc.uncarbon.module.oss.service.OssFileInfoService;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.exception.FileStorageRuntimeException;
import org.springframework.stereotype.Service;


/**
 * 文件上传下载门面
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OssUploadDownloadFacadeImpl implements OssUploadDownloadFacade {

    private final OssFileInfoService ossFileInfoService;
    private final FileStorageService fileStorageService;

    @Override
    public OssFileInfoBO findByHash(String md5) {
        return ossFileInfoService.getOneByMd5(md5);
    }

    @Override
    public OssFileInfoBO upload(byte[] fileBytes, @NonNull UploadFileAttributeDTO attr) throws BusinessException {
        FileInfo fileInfo;
        try {
            fileInfo = fileStorageService
                    .of(fileBytes)
                    .setOriginalFilename(attr.getOriginalFilename())
                    // 不手动指定，由框架自动生成存储文件名
                    .setSaveFilename(null)
                    .setContentType(attr.getContentType())
                    // 如果需要缩略图
                    // .setSaveThFilename()
                    // .setThContentType(contentType)
                    .upload();
        } catch (FileStorageRuntimeException fsre) {
            log.error("[文件上传下载门面][上传] FileStorageRuntimeException >> ", fsre);
            throw new BusinessException(OssErrorEnum.FILE_UPLOAD_FAILED);
        }

        log.info("[文件上传下载门面][上传] 正常上传成功 >> successFileInfo={}", fileInfo);

        /*
        记录存入 DB
         */
        return ossFileInfoService.save(fileInfo, attr);
    }

    @Override
    public OssFileDownloadReplyBO downloadById(Long fileInfoId) throws BusinessException {
        /*
        注：开启多租户后，因可能允许未登录下载，无法确定当前租户ID，导致查询 SQL 会错误地拼接 AND tenant_id = null 条件
        解决方法1：Controller层方法解禁 @SaCheckLogin 注解，强制要求在 url-params 中传递 token 才可下载
        解决方法2：后端使用 try-finally 临时设置特权租户上下文
                TenantContextHolder.setTenantContext(new TenantContext(HelioConstant.Tenant.DEFAULT_PRIVILEGED_TENANT_ID, "临时特权租户"))
                以绕过行级租户过滤器；数据源级租户同理，使其固定访问某个数据源来查询文件信息
        解决方法3：改造Controller层方法参数，增加传递一个租户ID字段，并在此切换上下文
         */
        OssFileInfoBO ossFileInfo = ossFileInfoService.getOneById(fileInfoId, true);

        /*
        这里请根据实际业务性质调整
        有的业务出于安全目的，不能暴露直链，只能通过服务端代理下载后，返回 byte[]
        有的业务没有限制，上传后文件完全可以直接通过对象存储直链下载，如此还能节约服务端上传带宽
        但本地存储又没有直链，只能通过文件ID；
        默认地，此处按【本地存储or对象存储直链为空：通过服务端代理下载；对象存储：通过对象存储直链下载】返回
         */
        boolean redirect2DirectUrl = false;
        byte[] fileBytes = null;
        if (
                OssFileInfoService.isLocalPlatform(ossFileInfo.getStoragePlatform())
                || CharSequenceUtil.isEmpty(ossFileInfo.getDirectUrl())
        ) {
            FileInfo fileInfo = OssFileInfoService.toFileInfo(ossFileInfo);
            try {
                fileBytes = fileStorageService.download(fileInfo).bytes();
            } catch (FileStorageRuntimeException fsre) {
                log.error("[文件上传下载门面][下载] FileStorageRuntimeException >> ", fsre);
                throw new BusinessException(OssErrorEnum.FILE_DOWNLOAD_FAILED);
            }
        } else {
            redirect2DirectUrl = true;
        }
        return OssFileDownloadReplyBO.builder()
                .redirect2DirectUrl(redirect2DirectUrl)
                .fileBytes(fileBytes)
                .directUrl(ossFileInfo.getDirectUrl())
                .storageFilename(ossFileInfo.getStorageFilenameFull())
                .build();
    }

    @Override
    public boolean isLocalPlatform(String storagePlatform) {
        return OssFileInfoService.isLocalPlatform(storagePlatform);
    }
}
