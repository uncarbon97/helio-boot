package cc.uncarbon.module.library.web;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.page.PageParam;
import cc.uncarbon.framework.core.page.PageResult;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.library.model.request.AdminInsertOrUpdateMemberDTO;
import cc.uncarbon.module.library.model.request.AdminListMemberDTO;
import cc.uncarbon.module.library.model.response.MemberBO;
import cc.uncarbon.module.library.service.MemberService;
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
 * 后台管理-会员管理接口
 *
 * @author Uncarbon
 */
@RequiredArgsConstructor
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Slf4j
@Api(value = "会员管理接口", tags = {"会员管理接口"})
@RequestMapping(SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/library/members")
@RestController
public class AdminMemberController {

    // 功能权限串前缀
    private static final String PERMISSION_PREFIX = "Member:" ;

    private final MemberService memberService;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "分页列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public ApiResult<PageResult<MemberBO>> list(PageParam pageParam, AdminListMemberDTO dto) {
        return ApiResult.data(memberService.adminList(pageParam, dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public ApiResult<MemberBO> getById(@PathVariable Long id) {
        return ApiResult.data(memberService.getOneById(id, true));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping
    public ApiResult<?> insert(@RequestBody @Valid AdminInsertOrUpdateMemberDTO dto) {
        memberService.adminInsert(dto);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public ApiResult<?> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateMemberDTO dto) {
        dto.setId(id);
        memberService.adminUpdate(dto);

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping
    public ApiResult<?> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        memberService.adminDelete(dto.getIds());

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "列表-下拉框专用", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/options")
    public ApiResult<List<MemberBO>> listOptions() {
        return ApiResult.data(memberService.adminListOptions());
    }

}
