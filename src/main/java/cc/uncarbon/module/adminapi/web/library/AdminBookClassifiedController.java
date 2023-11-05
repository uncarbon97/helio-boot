package cc.uncarbon.module.adminapi.web.library;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.library.model.request.AdminInsertOrUpdateBookClassifiedDTO;
import cc.uncarbon.module.library.model.response.BookClassifiedBO;
import cc.uncarbon.module.library.service.BookClassifiedService;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 后台管理-书籍类别管理接口
 *
 * @author Uncarbon
 */
@RequiredArgsConstructor
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Slf4j
@Api(value = "书籍类别管理接口", tags = {"书籍类别管理接口"})
@RequestMapping(value = {
        // 兼容旧的API路由前缀
        SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1,
        AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1"
})
@RestController
public class AdminBookClassifiedController {

    // 功能权限串前缀
    private static final String PERMISSION_PREFIX = "BookClassified:" ;

    private final BookClassifiedService bookClassifiedService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/library/bookClassifieds")
    public ApiResult<List<BookClassifiedBO>> list() {
        return ApiResult.data(bookClassifiedService.adminList());
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/library/bookClassifieds/{id}")
    public ApiResult<BookClassifiedBO> getById(@PathVariable Long id) {
        return ApiResult.data(bookClassifiedService.getOneById(id, true));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/library/bookClassifieds")
    public ApiResult<?> insert(@RequestBody @Valid AdminInsertOrUpdateBookClassifiedDTO dto) {
        bookClassifiedService.adminInsert(dto);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/library/bookClassifieds/{id}")
    public ApiResult<?> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateBookClassifiedDTO dto) {
        dto.setId(id);
        bookClassifiedService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping(value = "/library/bookClassifieds")
    public ApiResult<?> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        bookClassifiedService.adminDelete(dto.getIds());

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "列表-下拉框专用", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/library/bookClassifieds/options")
    public ApiResult<List<BookClassifiedBO>> listOptions() {
        return ApiResult.data(bookClassifiedService.adminListOptions());
    }

}
