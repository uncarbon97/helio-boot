package cc.uncarbon.test;

import cc.uncarbon.HelioBootApplication;
import cc.uncarbon.framework.core.exception.BusinessException;
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
    void init() {
        throw new BusinessException(500, "template test {} {} {} {} {}",
                tenantEnabled,
                helioProperties,
                helioProperties.getTenant().getEnabled(),
                helioProperties.getTenant().getIgnoredTables(),
                tenantSupport.getClass().isAssignableFrom(DefaultTenantSupport.class)
                );
    }
}
