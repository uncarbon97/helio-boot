package cc.uncarbon.module.adminapi.web.sys;


import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysDeptDTO;
import cc.uncarbon.module.sys.model.response.SysDeptBO;
import cc.uncarbon.module.sys.service.SysDeptService;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;


@SaCheckLogin(type = AdminStpUtil.TYPE)
@Tag(name = "部门管理接口")
@RequestMapping(value = AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminSysDeptController {

    private static final String PERMISSION_PREFIX = "SysDept:";

    private final SysDeptService sysDeptService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @Operation(summary = "列表")
    @GetMapping(value = "/sys/depts")
    public ApiResult<List<SysDeptBO>> list() {
        return ApiResult.data(sysDeptService.adminList());
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @Operation(summary = "详情")
    @GetMapping(value = "/sys/depts/{id}")
    public ApiResult<SysDeptBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysDeptService.getOneById(id, true));
    }

    @SysLog(value = "新增部门")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @Operation(summary = "新增")
    @PostMapping(value = "/sys/depts")
    public ApiResult<Void> insert(@RequestBody @Valid AdminInsertOrUpdateSysDeptDTO dto) {
        sysDeptService.adminInsert(dto);

        return ApiResult.success();
    }

    @SysLog(value = "编辑部门")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @Operation(summary = "编辑")
    @PutMapping(value = "/sys/depts/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateSysDeptDTO dto) {
        dto.setId(id);
        sysDeptService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SysLog(value = "删除部门")
    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @Operation(summary = "删除")
    @DeleteMapping(value = "/sys/depts")
    public ApiResult<Void> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysDeptService.adminDelete(dto.getIds());

        return ApiResult.success();
    }

}
