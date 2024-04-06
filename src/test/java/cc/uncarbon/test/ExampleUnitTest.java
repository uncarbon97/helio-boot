package cc.uncarbon.test;

import cc.uncarbon.HelioBootApplication;
import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.TenantContext;
import cc.uncarbon.framework.core.context.TenantContextHolder;
import cc.uncarbon.framework.core.context.UserContext;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.module.sys.model.response.SysRoleBO;
import cc.uncarbon.module.sys.service.SysRoleService;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 一个仅用于示例的单元测试
 *
 * @author Uncarbon
 */
@SpringBootTest(classes = HelioBootApplication.class)
class ExampleUnitTest {

    @Resource
    private SysRoleService sysRoleService;


    @BeforeEach
    public void init() {
        // 设置用户上下文
        UserContext userContext = new UserContext();
        userContext
                .setUserId(1L)
                .setUserName("超级管理员")
                // 用户类型, 根据单元测试需要修改
                .setUserTypeStr("ADMIN_USER");
        UserContextHolder.setUserContext(userContext);

        // 设置租户上下文
        TenantContext tenantContext = new TenantContext();
        tenantContext
                .setTenantId(HelioConstant.Tenant.DEFAULT_PRIVILEGED_TENANT_ID)
                .setTenantName("超级租户");
        TenantContextHolder.setTenantContext(tenantContext);
    }

    @Test
    void exampleTest() {
        List<SysRoleBO> selectOptions = sysRoleService.adminSelectOptions();
        Assertions.assertTrue(CollUtil.isNotEmpty(selectOptions));
    }
}
