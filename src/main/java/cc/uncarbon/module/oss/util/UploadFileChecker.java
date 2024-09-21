package cc.uncarbon.module.oss.util;

import cc.uncarbon.module.oss.enums.UploadFileCheckResultEnum;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;

/**
 * 前端上传文件检查器
 * 该检查器较为简易，请按照实际网络安全需要扩展
 */
@UtilityClass
public class UploadFileChecker {

    // 最小文件数量
    private static final int FILE_QTY_MIN = 1;
    // 最小文件尺寸
    private static final int FILE_SIZE_MIN = 1;

    // 文件尺寸 1MB
    public static final long FILE_SIZE_1MB = 1024 * 1024L;


    /**
     * 批量检查
     *
     * @param fileQtyMax        最大允许的文件数量
     * @param singleFileSizeMax 最大单文件尺寸，单位=KB
     * @param allFileSizeMax    最大多文件累计尺寸，单位=KB
     * @param allowedSuffixes   允许上传的文件后缀
     */
    public UploadFileCheckResultEnum check(Collection<MultipartFile> multipartFiles, int fileQtyMax,
                                           long singleFileSizeMax, long allFileSizeMax, String[] allowedSuffixes) {
        // 限制文件数量
        int fileQty = CollUtil.size(multipartFiles);
        if (fileQty < FILE_QTY_MIN) {
            return UploadFileCheckResultEnum.NO_FILE;
        } else if (fileQty > fileQtyMax) {
            return UploadFileCheckResultEnum.TOO_MANY_FILES;
        }
        // 限制所有文件总大小
        long allFileSize = multipartFiles.stream().mapToLong(MultipartFile::getSize).sum();
        // 除以1024
        allFileSize = allFileSize >> 10;
        if (allFileSize > allFileSizeMax) {
            return UploadFileCheckResultEnum.TOO_LARGE_FILE_SIZE;
        }

        // 每个文件单独检查
        for (MultipartFile item : multipartFiles) {
            UploadFileCheckResultEnum singleCheckRet = check(item, singleFileSizeMax, allowedSuffixes);
            if (singleCheckRet.isNotOK()) {
                return singleCheckRet;
            }
        }
        return UploadFileCheckResultEnum.OK;
    }

    /**
     * 单个检查
     * @param singleFileSizeMax 最大单文件尺寸，单位=KB
     * @param allowedSuffixes   允许上传的文件后缀
     */
    public UploadFileCheckResultEnum check(@Nonnull MultipartFile multipartFile, long singleFileSizeMax, String[] allowedSuffixes) {
        long fileSize = multipartFile.getSize();

        // 确定文件大小区间
        if (fileSize < FILE_SIZE_MIN) {
            return UploadFileCheckResultEnum.EMPTY_FILE;
        }
        // 除以1024
        fileSize = fileSize >> 10;
        if (fileSize > singleFileSizeMax) {
            return UploadFileCheckResultEnum.TOO_LARGE_FILE_SIZE;
        }

        // 确定后缀名
        String suffix = FileNameUtil.getSuffix(multipartFile.getOriginalFilename());
        if (CharSequenceUtil.isNotBlank(suffix)) {
            suffix = suffix.toLowerCase(Locale.ROOT);
        }
        // 检查后缀名
        if (Objects.isNull(suffix) || !ArrayUtil.contains(allowedSuffixes, suffix)) {
            return UploadFileCheckResultEnum.ILLEGAL_FILE_SUFFIX;
        }
        return UploadFileCheckResultEnum.OK;
    }
}
