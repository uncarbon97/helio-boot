package cc.uncarbon.module.adminapi.web.sys;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.helper.RolePermissionCacheHelper;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.model.request.AdminBindRoleMenuRelationDTO;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysRoleDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysRoleDTO;
import cc.uncarbon.module.sys.model.response.SysRoleBO;
import cc.uncarbon.module.sys.service.SysRoleService;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;


@SaCheckLogin(type = AdminStpUtil.TYPE)
@Api(value = "后台角色管理接口", tags = {"后台角色管理接口"})
@RequestMapping(value = {
        // 兼容旧的API路由前缀
        SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1,
        AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1"
})
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminSysRoleController {

    private static final String PERMISSION_PREFIX = "SysRole:";

    private final SysRoleService sysRoleService;

    private final RolePermissionCacheHelper rolePermissionCacheHelper;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "分页列表")
    @GetMapping(value = "/sys/roles")
    public ApiResult<PageResult<SysRoleBO>> list(PageParam pageParam, AdminListSysRoleDTO dto) {
        return ApiResult.data(sysRoleService.adminList(pageParam, dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情")
    @GetMapping(value = "/sys/roles/{id}")
    public ApiResult<SysRoleBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysRoleService.getOneById(id, true));
    }

    @SysLog(value = "新增后台角色")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增")
    @PostMapping(value = "/sys/roles")
    public ApiResult<Void> insert(@RequestBody @Valid AdminInsertOrUpdateSysRoleDTO dto) {
        dto.setTenantId(null);
        sysRoleService.adminInsert(dto);

        return ApiResult.success();
    }

    @SysLog(value = "编辑后台角色")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑")
    @PutMapping(value = "/sys/roles/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateSysRoleDTO dto) {
        dto
                .setTenantId(null)
                .setId(id);
        sysRoleService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SysLog(value = "删除后台角色")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/sys/roles")
    public ApiResult<Void> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysRoleService.adminDelete(dto.getIds());

        // 角色删除时，删除对应缓存键
        rolePermissionCacheHelper.deleteCache(dto.getIds());

        return ApiResult.success();
    }

    @SysLog(value = "绑定角色与菜单关联关系")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + "bindMenus")
    @ApiOperation(value = "绑定角色与菜单关联关系")
    @PutMapping(value = "/sys/roles/{id}/menus")
    public ApiResult<Void> bindMenus(@PathVariable Long id, @RequestBody @Valid AdminBindRoleMenuRelationDTO dto) {
        dto.setRoleId(id);
        Set<String> newPermissions = sysRoleService.adminBindMenus(dto);

        // 覆盖更新缓存
        rolePermissionCacheHelper.putCache(dto.getRoleId(), newPermissions);

        return ApiResult.success();
    }

}
