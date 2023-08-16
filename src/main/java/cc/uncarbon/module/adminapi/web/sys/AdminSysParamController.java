package cc.uncarbon.module.adminapi.web.sys;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysParamDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysParamDTO;
import cc.uncarbon.module.sys.model.response.SysParamBO;
import cc.uncarbon.module.sys.service.SysParamService;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Slf4j
@Api(value = "系统参数管理接口", tags = {"系统参数管理接口"})
@RequestMapping(value = {
        // 兼容旧的API路由前缀
        SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1,
        AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1"
})
@RestController
public class AdminSysParamController {

    private static final String PERMISSION_PREFIX = "SysParam:";

    private final SysParamService sysParamService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/sys/params")
    public ApiResult<PageResult<SysParamBO>> list(PageParam pageParam, AdminListSysParamDTO dto) {
        return ApiResult.data(sysParamService.adminList(pageParam, dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情")
    @GetMapping(value = "/sys/params/{id}")
    public ApiResult<SysParamBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysParamService.getOneById(id, true));
    }

    @SysLog(value = "新增系统参数")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增")
    @PostMapping(value = "/sys/params")
    public ApiResult<?> insert(@RequestBody @Valid AdminInsertOrUpdateSysParamDTO dto) {
        sysParamService.adminInsert(dto);

        return ApiResult.success();
    }

    @SysLog(value = "编辑系统参数")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑")
    @PutMapping(value = "/sys/params/{id}")
    public ApiResult<?> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateSysParamDTO dto) {
        dto.setId(id);
        sysParamService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SysLog(value = "删除系统参数")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/sys/params")
    public ApiResult<?> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysParamService.adminDelete(dto.getIds());

        return ApiResult.success();
    }

}
