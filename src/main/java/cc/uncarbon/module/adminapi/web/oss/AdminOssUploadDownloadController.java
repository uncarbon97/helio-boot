package cc.uncarbon.module.adminapi.web.oss;

import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.adminapi.enums.AdminApiErrorEnum;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cc.uncarbon.module.oss.enums.UploadFileCheckResultEnum;
import cc.uncarbon.module.oss.facade.OssUploadDownloadFacade;
import cc.uncarbon.module.oss.model.request.UploadFileAttributeDTO;
import cc.uncarbon.module.oss.model.response.OssFileDownloadReplyBO;
import cc.uncarbon.module.oss.model.response.OssFileInfoBO;
import cc.uncarbon.module.oss.model.response.OssFileUploadResultVO;
import cc.uncarbon.module.oss.util.UploadFileChecker;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.Header;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Tag(name = "后台管理-上传、下载文件接口")
@RequestMapping(value = AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminOssUploadDownloadController {

    private final OssUploadDownloadFacade ossUploadDownloadFacade;


    @Operation(summary = "上传文件")
    @PostMapping(value = "/oss/files")
    // 约束：登录后才能上传   👇 后台管理对应的鉴权工具类
    @SaCheckLogin(type = AdminStpUtil.TYPE)
    public ApiResult<OssFileUploadResultVO> upload(
            @RequestPart MultipartFile file, @RequestPart(required = false) @Valid UploadFileAttributeDTO attr,
            HttpServletRequest request
    ) throws IOException {
        AdminApiErrorEnum.UPLOAD_FILE_NOT_EXIST.assertNotNull(file);
        UploadFileCheckResultEnum checkResult = UploadFileChecker.check(file,
                // 默认只能上传100MB之内的文件，且约束后缀名
                UploadFileChecker.FILE_SIZE_1MB * 100,
                new String[]{"jpg", "png", "webp", "gif", "xlsx"});
        if (checkResult.isNotOK()) {
            throw new BusinessException(checkResult.getLabel());
        }

        /*
        1. 已存在相同 MD5 文件，直接返回 URL
         */
        String md5 = DigestUtil.md5Hex(file.getBytes());
        OssFileInfoBO bo = ossUploadDownloadFacade.findByHash(md5);
        if (bo == null) {

            /*
            2. 普通上传
             */
            attr = ObjectUtil.defaultIfNull(attr, UploadFileAttributeDTO::new);
            attr
                    .setOriginalFilename(file.getOriginalFilename())
                    .setContentType(file.getContentType())
                    .setMd5(md5)
                    // 手动覆盖前端传参
                    .setPlatform(null)
                    .setUseOriginalFilenameAsDownloadFileName(false);
            bo = ossUploadDownloadFacade.upload(file.getBytes(), attr);
        }

        return ApiResult.data(this.toUploadResult(bo, request.getRequestURL().toString(), file.getOriginalFilename()));
    }

    @Operation(summary = "下载文件(根据文件ID)")
    @GetMapping(value = "/oss/files/{id}")
    // 如果需要登录后才能下载，请解禁下方注解；注意是👇 后台管理对应的鉴权工具类
    // @SaCheckLogin(type = AdminStpUtil.TYPE)
    public void download(@PathVariable Long id, HttpServletResponse response) throws IOException {
        OssFileDownloadReplyBO reply = ossUploadDownloadFacade.downloadById(id);

        if (reply.isRedirect2DirectUrl()) {
            // 302重定向
            response.sendRedirect(reply.getDirectUrl());
            return;
        }

        // 普通下载
        response.setHeader(Header.CONTENT_TYPE.getValue(), MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        String downFileName = URLEncoder.encode(reply.getStorageFilename(), StandardCharsets.UTF_8);
        response.setHeader(Header.CONTENT_DISPOSITION.getValue(), "attachment;filename=" + downFileName);

        IoUtil.write(response.getOutputStream(), false, reply.getFileBytes());
    }

    /**
     * 将 OssFileInfoBO 转换为 OssFileUploadResultVO
     */
    private OssFileUploadResultVO toUploadResult(OssFileInfoBO ossFileInfo, String requestUrl, String originalFilename) {
        OssFileUploadResultVO ret = new OssFileUploadResultVO()
                .setFileId(ossFileInfo.getId())
                .setFilename(ossFileInfo.getStorageFilenameFull())
                // 返回本次上传文件的原始文件名
                .setOriginalFilename(originalFilename);

        /*
        这里请根据实际业务性质调整
        有的业务出于安全目的，上传后文件只能通过文件ID才能下载
        有的业务没有限制，上传后文件完全可以直接通过对象存储直链下载
        但本地存储又没有直链，只能通过文件ID；
        默认地，此处按【本地存储or对象存储直链为空：通过文件ID下载；对象存储：通过对象存储直链下载】返回 url
         */
        if (
                ossUploadDownloadFacade.isLocalPlatform(ossFileInfo.getStoragePlatform())
                        || CharSequenceUtil.isEmpty(ossFileInfo.getDirectUrl())
        ) {
            ret.setUrl(
                    // 默认接口风格为 RESTful，下载即为最后拼接“/{文件ID}”
                    String.format("%s/%s", requestUrl, ossFileInfo.getId())
            );
        } else {
            ret.setUrl(ossFileInfo.getDirectUrl());
        }

        return ret;
    }
}
