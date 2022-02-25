package cc.uncarbon.test;

import cc.uncarbon.HelioBootApplication;
import cc.uncarbon.framework.core.props.HelioProperties;
import cc.uncarbon.framework.crud.support.TenantSupport;
import cc.uncarbon.framework.crud.support.impl.DefaultTenantSupport;
import javax.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Uncarbon
 */
@SpringBootTest(classes = HelioBootApplication.class)
class InjectUnitTest {

    @Resource
    private TenantSupport tenantSupport;

    @Resource
    private HelioProperties helioProperties;

    @Value("${helio.tenant.enabled}")
    private Boolean tenantEnabled;

    @Test
    public void init() {
        System.out.println(tenantEnabled);
        System.out.println(helioProperties);
        System.out.println(helioProperties.getTenant().getEnabled());
        System.out.println(helioProperties.getTenant().getIgnoredTables());
        System.out.println(tenantSupport.getClass().isAssignableFrom(DefaultTenantSupport.class));
    }
}
