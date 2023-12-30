package cc.uncarbon.test;

import cc.uncarbon.HelioBootApplication;
import cc.uncarbon.framework.i18n.util.I18nUtil;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

/**
 * 基础单元测试
 * @author Uncarbon
 */
@SuppressWarnings("squid:S2699")
@SpringBootTest(classes = HelioBootApplication.class)
class BasicUnitTest {

    @Test
    void testI18nMessage() {
        String msg = I18nUtil.messageOf(SysErrorEnum.INVALID_TENANT);
        Assert.isTrue("所属租户无效".equals(msg));

        msg = I18nUtil.messageOf(Locale.US, SysErrorEnum.INCORRECT_PIN_OR_PWD);
        Assert.isTrue("Incorrect username or password".equals(msg));

        msg = I18nUtil.messageOf(Locale.CHINA, SysErrorEnum.DISABLED_TENANT);
        Assert.isTrue("所属租户已禁用".equals(msg));

        msg = I18nUtil.messageOf(Locale.US, "INCORRECT_PIN_OR_PWD");
        Assert.isTrue(msg == null);

        msg = I18nUtil.messageOf(Locale.US, "SysErrorEnum.UUID_CANNOT_BE_BLANK");
        Assert.isTrue("UUID cannot be blank".equals(msg));
    }
}
