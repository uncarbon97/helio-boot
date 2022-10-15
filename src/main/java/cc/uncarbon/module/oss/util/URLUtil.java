package cc.uncarbon.module.oss.util;

import cn.hutool.core.util.StrUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 定制 URLUtil
 * 继承自 cn.hutool.core.util.URLUtil
 *
 * @author Uncarbon
 */
public class URLUtil extends cn.hutool.core.util.URLUtil {

    /**
     * 取完整域名（protocol + domain + port），最后不带"/"
     * @param urlStr URL文本
     */
    public static String getFullDomain(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return String.format("%s://%s:%s", url.getProtocol(), url.getHost(), url.getPort());
        } catch (MalformedURLException e) {
            // 走兜底方案
        }

        // 兜底方案，直接替换掉 path 部分
        return StrUtil.replace(urlStr, getPath(urlStr), StrUtil.EMPTY);
    }

}
