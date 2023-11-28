package cc.uncarbon.test;


import cc.uncarbon.framework.core.context.UserContext;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.module.sys.model.response.SysMenuBO;
import cc.uncarbon.module.sys.service.SysDeptService;
import cc.uncarbon.module.sys.service.SysMenuService;
import cc.uncarbon.module.sys.service.SysRoleService;
import cc.uncarbon.module.sys.util.PwdUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 默认单元测试
 */
@SpringBootTest(classes = SysServiceApplication.class)
class DefaultUnitTest {

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleService sysRoleService;


    @BeforeEach
    public void setCurrentUser() {
        UserContext userContext = new UserContext();
        userContext
                .setUserId(1L)
                .setUserName("超级管理员")
                // 用户类型, 根据单元测试需要修改
                .setUserTypeStr("ADMIN_USER")
        ;
        UserContextHolder.setUserContext(userContext);
    }

    @Test
    void case1() {
        List<SysMenuBO> sideMenu = sysMenuService.adminListSideMenu();
        System.out.println("侧边菜单");
        System.out.println(JSONUtil.toJsonStr(sideMenu));

    }

    /**
     * 计算加密后的密码
     * 用于快速重置密码
     */
    @Test
    void calculateEncryptedPassword() {
        String salt = IdUtil.randomUUID();
        System.out.println("salt=" + salt);
        System.out.println("EncryptedPassword=" + PwdUtil.encrypt("jiagongsi", salt));
    }

}
