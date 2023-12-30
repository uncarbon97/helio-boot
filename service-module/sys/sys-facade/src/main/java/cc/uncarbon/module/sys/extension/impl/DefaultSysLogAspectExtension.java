package cc.uncarbon.module.sys.extension.impl;

import cc.uncarbon.module.sys.extension.SysLogAspectExtension;
import cc.uncarbon.module.sys.model.response.IPLocationBO;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultSysLogAspectExtension implements SysLogAspectExtension {

    @Override
    public IPLocationBO queryIPLocation(String ip) {
        if (CharSequenceUtil.contains(ip, StrPool.COLON)) {
            // 仅根据冒号简易判断；暂不支持IPv6地址
            return IPLocationBO.unknown();
        }

        if (NetUtil.isInnerIP(ip)) {
            return IPLocationBO.intranet();
        }

        // 该API主要支持中国内地
        HttpRequest httpRequest = HttpRequest.get("http://whois.pconline.com.cn/ipJson.jsp")
                .form("ip", ip)
                .form("json", Boolean.TRUE.toString())
                .charset(CharsetUtil.CHARSET_GBK)
                .timeout(5000);
        try (HttpResponse httpResponse = httpRequest.execute()) {
            String repStr = httpResponse.body();
            if (JSONUtil.isTypeJSONObject(repStr)) {
                /*
                返回文本是JSON对象
                example1:
                {"ip":"39.156.66.10","pro":"北京市","proCode":"110000","city":"北京市","cityCode":"110000","region":"","regionCode":"0","addr":"北京市 移通","regionNames":"","err":""}

                example2:
                {"ip":"1.1.1.1","pro":"","proCode":"999999","city":"","cityCode":"0","region":"","regionCode":"0","addr":" 美国APNIC&CloudFlare公共DNS服务器","regionNames":"","err":"noprovince"}

                example3:
                {"ip":"8.8.8.8","pro":"","proCode":"999999","city":"","cityCode":"0","region":"","regionCode":"0","addr":" 美国","regionNames":"","err":"noprovince"}
                 */
                JSONObject repJson = JSONUtil.parseObj(repStr);
                String pro = repJson.getStr("pro");
                String err = repJson.getStr("err");
                if (CharSequenceUtil.isEmpty(pro) || "noprovince".equals(err)) {
                    // 可能是非中国内地IP
                    String regionName = repJson.getStr("addr");
                    return IPLocationBO.unknown().setRegionName(regionName);
                }
                return IPLocationBO.inChina()
                        .setProvinceName(pro)
                        .setCityName(repJson.getStr("city"));
            }
        } catch (Exception e) {
            log.error("[SysLog切面][查询IP地址属地异常] >> ip={}  \n", ip, e);
        }

        return IPLocationBO.unknown();
    }
}
