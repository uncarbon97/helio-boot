package cc.uncarbon.module.sys.controller;


import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysDeptDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysDeptDTO;
import cc.uncarbon.module.sys.model.response.SysDeptBO;
import cc.uncarbon.module.sys.service.SysDeptService;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Uncarbon
 */
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Slf4j
@Api(value = "部门管理接口", tags = {"部门管理接口"})
@RequestMapping(SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/sys/depts")
@RestController
public class AdminSysDeptController {

    private static final String PERMISSION_PREFIX = "SysDept:";

    @Resource
    private SysDeptService sysDeptService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public ApiResult<List<SysDeptBO>> list(AdminListSysDeptDTO dto) {
        return ApiResult.data(sysDeptService.adminList(dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public ApiResult<SysDeptBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysDeptService.getOneById(id, true));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping
    public ApiResult<?> insert(@RequestBody @Valid AdminInsertOrUpdateSysDeptDTO dto) {
        sysDeptService.adminInsert(dto);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public ApiResult<?> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateSysDeptDTO dto) {
        dto.setId(id);
        sysDeptService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping
    public ApiResult<?> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysDeptService.adminDelete(dto.getIds());

        return ApiResult.success();
    }

}
