package cc.uncarbon.test;

import cc.uncarbon.HelioBootApplication;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.module.sys.model.request.AdminListSysLogDTO;
import cc.uncarbon.module.sys.model.response.SysLogBO;
import cc.uncarbon.module.sys.service.SysLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Uncarbon
 */
@SpringBootTest(classes = HelioBootApplication.class)
class SysLogServiceUnitTest {

    @Resource
    private SysLogService sysLogService;


    @BeforeEach
    public void init() {
        // 可以在这里进行初始化操作, 如设置用户上下文等
        // UserContextHolder.setUserContext();
    }

    @Test
    void testAdminList() {
        List<SysLogBO> ret = sysLogService.adminList(new PageParam(1, 10000), new AdminListSysLogDTO()).getRecords();
        System.out.println(ret);
    }
}
