package cc.uncarbon.module.oss.enums;

import cc.uncarbon.framework.core.enums.HelioBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 前端上传文件检查结果枚举类
 */
@AllArgsConstructor
@Getter
public enum UploadFileCheckResultEnum implements HelioBaseEnum<Integer> {

    OK(200, "正常"),
    NO_FILE(401, "缺少欲上传的文件"),
    TOO_MANY_FILES(402, "欲上传的文件数量超出限制"),
    TOO_LARGE_FILE_SIZE(403, "欲上传的文件大小超出限制"),
    EMPTY_FILE(404, "欲上传的文件为空"),
    ILLEGAL_FILE_SUFFIX(405, "欲上传的文件类型超出限制"),;

    @EnumValue
    private final Integer value;
    private final String label;

    public boolean isOK() {
        return this == OK;
    }

    public boolean isNotOK() {
        return !isOK();
    }

}
