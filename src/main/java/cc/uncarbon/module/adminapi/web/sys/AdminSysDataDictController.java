package cc.uncarbon.module.adminapi.web.sys;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.model.request.AdminSysDataDictClassifiedInsertOrUpdateDTO;
import cc.uncarbon.module.sys.model.request.AdminSysDataDictClassifiedListDTO;
import cc.uncarbon.module.sys.model.request.AdminSysDataDictItemInsertOrUpdateDTO;
import cc.uncarbon.module.sys.model.request.AdminSysDataDictItemListDTO;
import cc.uncarbon.module.sys.model.response.SysDataDictClassifiedBO;
import cc.uncarbon.module.sys.model.response.SysDataDictItemBO;
import cc.uncarbon.module.sys.service.SysDataDictService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@SaCheckLogin(type = AdminStpUtil.TYPE)
@Tag(name = "数据字典管理接口")
@RequestMapping(value = AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminSysDataDictController {

    private static final String PERMISSION_PREFIX = "SysDataDict:";

    private final SysDataDictService sysDataDictService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @Operation(summary = "分页列表数据字典分类")
    @GetMapping(value = "/sys/data-dict/classifieds")
    public ApiResult<PageResult<SysDataDictClassifiedBO>> list(PageParam pageParam, AdminSysDataDictClassifiedListDTO dto) {
        return ApiResult.data(sysDataDictService.adminListClassified(pageParam, dto));
    }

    @SysLog(value = "新增数据字典分类")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @Operation(summary = "新增数据字典分类")
    @PostMapping(value = "/sys/data-dict/classifieds")
    public ApiResult<Void> insert(@RequestBody @Valid AdminSysDataDictClassifiedInsertOrUpdateDTO dto) {
        sysDataDictService.adminInsertClassified(dto);

        return ApiResult.success();
    }

    @SysLog(value = "编辑数据字典分类")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @Operation(summary = "编辑数据字典分类")
    @PutMapping(value = "/sys/data-dict/classifieds/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid AdminSysDataDictClassifiedInsertOrUpdateDTO dto) {
        dto.setId(id);
        sysDataDictService.adminUpdateClassified(dto);

        return ApiResult.success();
    }

    @SysLog(value = "删除数据字典分类")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @Operation(summary = "删除数据字典分类")
    @DeleteMapping(value = "/sys/data-dict/classifieds")
    public ApiResult<Void> deleteClassified(@RequestBody @Valid IdsDTO<Long> dto) {
        sysDataDictService.adminDeleteClassified(dto.getIds());

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @Operation(summary = "分页列表数据字典分类下的字典项")
    @GetMapping(value = "/sys/data-dict/classifieds/{classifiedId}/items")
    public ApiResult<PageResult<SysDataDictItemBO>> list(@PathVariable Long classifiedId, PageParam pageParam, AdminSysDataDictItemListDTO dto) {
        dto.setClassifiedId(classifiedId);
        return ApiResult.data(sysDataDictService.adminListItem(pageParam, dto));
    }

    @SysLog(value = "新增数据字典项")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @Operation(summary = "新增数据字典项")
    @PostMapping(value = "/sys/data-dict/classifieds/{classifiedId}/items")
    public ApiResult<Void> insert(@PathVariable Long classifiedId, @RequestBody @Valid AdminSysDataDictItemInsertOrUpdateDTO dto) {
        dto.setClassifiedId(classifiedId);
        sysDataDictService.adminInsertItem(dto);

        return ApiResult.success();
    }

    @SysLog(value = "编辑数据字典项")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @Operation(summary = "编辑数据字典项")
    @PutMapping(value = "/sys/data-dict/classifieds/{classifiedId}/items/{id}")
    public ApiResult<Void> update(@PathVariable Long classifiedId, @PathVariable Long id, @RequestBody @Valid AdminSysDataDictItemInsertOrUpdateDTO dto) {
        dto
                .setId(id)
                .setClassifiedId(classifiedId);
        sysDataDictService.adminUpdateItem(dto);

        return ApiResult.success();
    }

    @SysLog(value = "删除数据字典项")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @Operation(summary = "删除数据字典项")
    @DeleteMapping(value = "/sys/data-dict/classifieds/{classifiedId}/items")
    public ApiResult<Void> deleteItem(@PathVariable Long classifiedId, @RequestBody @Valid IdsDTO<Long> dto) {
        sysDataDictService.adminDeleteItem(dto.getIds(), classifiedId);

        return ApiResult.success();
    }
}
