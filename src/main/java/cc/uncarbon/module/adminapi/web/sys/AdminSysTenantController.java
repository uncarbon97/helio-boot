package cc.uncarbon.module.adminapi.web.sys;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.facade.SysTenantFacade;
import cc.uncarbon.module.sys.model.request.AdminInsertSysTenantDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysTenantDTO;
import cc.uncarbon.module.sys.model.request.AdminUpdateSysTenantDTO;
import cc.uncarbon.module.sys.model.response.SysTenantBO;
import cc.uncarbon.module.sys.service.SysTenantService;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@SaCheckLogin(type = AdminStpUtil.TYPE)
@Api(value = "系统租户管理接口", tags = {"系统租户管理接口"})
@RequestMapping(value = {
        // 兼容旧的API路由前缀
        SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1,
        AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1"
})
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminSysTenantController {

    private static final String PERMISSION_PREFIX = "SysTenant:";

    private final SysTenantService sysTenantService;

    private final SysTenantFacade sysTenantFacade;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/sys/tenants")
    public ApiResult<PageResult<SysTenantBO>> list(PageParam pageParam, AdminListSysTenantDTO dto) {
        return ApiResult.data(sysTenantService.adminList(pageParam, dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情")
    @GetMapping(value = "/sys/tenants/{id}")
    public ApiResult<SysTenantBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysTenantService.getOneById(id, true));
    }

    @SysLog(value = "新增系统租户")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增")
    @PostMapping(value = "/sys/tenants")
    public ApiResult<Void> insert(@RequestBody @Valid AdminInsertSysTenantDTO dto) {
        sysTenantFacade.adminInsert(dto);

        return ApiResult.success();
    }

    @SysLog(value = "编辑系统租户")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑")
    @PutMapping(value = "/sys/tenants/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid AdminUpdateSysTenantDTO dto) {
        dto.setId(id);
        sysTenantService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SysLog(value = "删除系统租户")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/sys/tenants")
    public ApiResult<Void> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysTenantFacade.adminDelete(dto.getIds());

        return ApiResult.success();
    }

}
