package cc.uncarbon.module.sys.web.sys;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.model.request.AdminListSysLogDTO;
import cc.uncarbon.module.sys.model.response.SysLogBO;
import cc.uncarbon.module.sys.service.SysLogService;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Slf4j
@Api(value = "系统日志管理接口", tags = {"系统日志管理接口"})
@RequestMapping(SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/sys/logs")
@RestController
public class AdminSysLogController {

    private static final String PERMISSION_PREFIX = "SysLog:";

    private final SysLogService sysLogService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "分页列表")
    @GetMapping
    public ApiResult<PageResult<SysLogBO>> list(PageParam pageParam, AdminListSysLogDTO dto) {
        return ApiResult.data(sysLogService.adminList(pageParam, dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情")
    @GetMapping(value = "/{id}")
    public ApiResult<SysLogBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysLogService.getOneById(id, true));
    }

}
