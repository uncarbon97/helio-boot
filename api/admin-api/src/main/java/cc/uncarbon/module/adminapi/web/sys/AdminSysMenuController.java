package cc.uncarbon.module.adminapi.web.sys;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysMenuDTO;
import cc.uncarbon.module.sys.model.response.SysMenuBO;
import cc.uncarbon.module.sys.service.SysMenuService;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@SaCheckLogin(type = AdminStpUtil.TYPE)
@Api(value = "后台菜单管理接口", tags = {"后台菜单管理接口"})
@RequestMapping(value = {
        // 兼容旧的API路由前缀
        SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1,
        AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1"
})
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminSysMenuController {

    private static final String PERMISSION_PREFIX = "SysMenu:";

    private final SysMenuService sysMenuService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "列表")
    @GetMapping(value = "/sys/menus")
    public ApiResult<List<SysMenuBO>> list() {
        return ApiResult.data(sysMenuService.adminList());
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情")
    @GetMapping(value = "/sys/menus/{id}")
    public ApiResult<SysMenuBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysMenuService.getOneById(id, true));
    }

    @SysLog(value = "新增后台菜单")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增")
    @PostMapping(value = "/sys/menus")
    public ApiResult<Void> insert(@RequestBody @Valid AdminInsertOrUpdateSysMenuDTO dto) {
        sysMenuService.adminInsert(dto);

        return ApiResult.success();
    }

    @SysLog(value = "编辑后台菜单")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑")
    @PutMapping(value = "/sys/menus/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateSysMenuDTO dto) {
        dto.setId(id);
        sysMenuService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SysLog(value = "删除后台菜单")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/sys/menus")
    public ApiResult<Void> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysMenuService.adminDelete(dto.getIds());

        return ApiResult.success();
    }

    @ApiOperation(value = "取侧边菜单")
    @GetMapping("/sys/menus/side")
    public ApiResult<List<SysMenuBO>> adminListSideMenu() {
        return ApiResult.data(sysMenuService.adminListSideMenu());
    }

    @ApiOperation(value = "取所有可见菜单")
    @GetMapping("/sys/menus/all")
    public ApiResult<List<SysMenuBO>> adminListVisibleMenu() {
        return ApiResult.data(sysMenuService.adminListVisibleMenu());
    }

}