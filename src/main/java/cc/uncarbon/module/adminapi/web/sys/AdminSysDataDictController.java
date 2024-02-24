package cc.uncarbon.module.adminapi.web.sys;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysDataDictDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysDataDictDTO;
import cc.uncarbon.module.sys.model.response.SysDataDictBO;
import cc.uncarbon.module.sys.service.SysDataDictService;
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
@Api(value = "数据字典管理接口", tags = {"数据字典管理接口"})
@RequestMapping(value = {
        // 兼容旧的API路由前缀
        SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1,
        AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1"
})
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminSysDataDictController {

    private static final String PERMISSION_PREFIX = "SysDataDict:";

    private final SysDataDictService sysDataDictService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "分页列表")
    @GetMapping(value = {
            "/sys/data-dicts",
            // 兼容旧的API路由
            "/sys/dataDicts"
    })
    public ApiResult<PageResult<SysDataDictBO>> list(PageParam pageParam, AdminListSysDataDictDTO dto) {
        return ApiResult.data(sysDataDictService.adminList(pageParam, dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情")
    @GetMapping(value = {
            "/sys/data-dicts/{id}",
            // 兼容旧的API路由
            "/sys/dataDicts/{id}"
    })
    public ApiResult<SysDataDictBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysDataDictService.getOneById(id, true));
    }

    @SysLog(value = "新增数据字典")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增")
    @PostMapping(value = {
            "/sys/data-dicts",
            // 兼容旧的API路由
            "/sys/dataDicts"
    })
    public ApiResult<Void> insert(@RequestBody @Valid AdminInsertOrUpdateSysDataDictDTO dto) {
        sysDataDictService.adminInsert(dto);

        return ApiResult.success();
    }

    @SysLog(value = "编辑数据字典")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑")
    @PutMapping(value = {
            "/sys/data-dicts/{id}",
            // 兼容旧的API路由
            "/sys/dataDicts/{id}"
    })
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateSysDataDictDTO dto) {
        dto.setId(id);
        sysDataDictService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SysLog(value = "删除数据字典")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除")
    @DeleteMapping(value = {
            "/sys/data-dicts",
            // 兼容旧的API路由
            "/sys/dataDicts"
    })
    public ApiResult<Void> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysDataDictService.adminDelete(dto.getIds());

        return ApiResult.success();
    }

}
