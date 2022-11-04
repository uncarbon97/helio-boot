package cc.uncarbon.module.library.web;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.library.model.request.AdminInsertOrUpdateBookBorrowingDTO;
import cc.uncarbon.module.library.model.request.AdminListBookBorrowingDTO;
import cc.uncarbon.module.library.model.response.BookBorrowingBO;
import cc.uncarbon.module.library.service.BookBorrowingService;
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


/**
 * 后台管理-书籍借阅记录管理接口
 *
 * @author Uncarbon
 */
@RequiredArgsConstructor
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Slf4j
@Api(value = "书籍借阅记录管理接口", tags = {"书籍借阅记录管理接口"})
@RequestMapping(SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/library/bookBorrowings")
@RestController
public class AdminBookBorrowingController {

    // 功能权限串前缀
    private static final String PERMISSION_PREFIX = "BookBorrowing:" ;

    private final BookBorrowingService bookBorrowingService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "分页列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public ApiResult<PageResult<BookBorrowingBO>> list(PageParam pageParam, AdminListBookBorrowingDTO dto) {
        return ApiResult.data(bookBorrowingService.adminList(pageParam, dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public ApiResult<BookBorrowingBO> getById(@PathVariable Long id) {
        return ApiResult.data(bookBorrowingService.getOneById(id, true));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping
    public ApiResult<?> insert(@RequestBody @Valid AdminInsertOrUpdateBookBorrowingDTO dto) {
        bookBorrowingService.adminInsert(dto);

        return ApiResult.success();
    }

    /*
    不允许编辑
     */

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping
    public ApiResult<?> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        bookBorrowingService.adminDelete(dto.getIds());

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + "return")
    @ApiOperation(value = "确认归还", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/{id}/return")
    public ApiResult<?> return_(@PathVariable Long id) {
        bookBorrowingService.adminReturn(id);

        return ApiResult.success();
    }

}
