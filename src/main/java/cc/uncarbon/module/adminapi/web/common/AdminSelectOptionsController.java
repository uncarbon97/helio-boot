package cc.uncarbon.module.adminapi.web.common;


import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.adminapi.model.response.SelectOptionItemVO;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cc.uncarbon.module.sys.model.response.SysRoleBO;
import cc.uncarbon.module.sys.service.SysRoleService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 约束：登录后才能使用   👇 后台管理对应的鉴权工具类
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Api(value = "后台管理-下拉框数据源接口", tags = {"后台管理-下拉框数据源接口"})
@RequestMapping(AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminSelectOptionsController {

    private final SysRoleService sysRoleService;


    /*
    这里统一存放所有用于后台管理的下拉框数据源接口
    避免多人协作时，不知道原来是否已经有了，或者写在某个边边角角里
    造成重复开发
     */

    @ApiOperation(value = "后台角色下拉框")
    @GetMapping(value = "/select-options/roles")    
    public ApiResult<List<SelectOptionItemVO>> roles() {
        return ApiResult.data(
                SelectOptionItemVO.listOf(sysRoleService.adminSelectOptions(), SysRoleBO::getId, SysRoleBO::getTitle)
        );
    }

}
