package cc.uncarbon.module.adminapi.web.common;


import cc.uncarbon.framework.core.enums.GenderEnum;
import cc.uncarbon.framework.core.enums.YesOrNoEnum;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.adminapi.constant.AdminApiConstant;
import cc.uncarbon.module.adminapi.model.response.SelectOptionItemVO;
import cc.uncarbon.module.sys.model.response.SysDeptBO;
import cc.uncarbon.module.sys.service.SysDeptService;
import cc.uncarbon.module.adminapi.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 约束：登录后才能使用   👇 后台管理对应的鉴权工具类
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Api(value = "后台管理-下拉框数据源接口", tags = {"后台管理-下拉框数据源接口"})
@RequestMapping(AdminApiConstant.HTTP_API_URL_PREFIX + "/api/v1")
@RequiredArgsConstructor
@RestController
@Slf4j
public class AdminSelectOptionsController {

    private final SysDeptService sysDeptService;


    /*
    这里统一存放所有用于后台管理的下拉框数据源接口
    避免多人协作时，不知道原来是否已经有了，或者写在某个边边角角里
    造成重复开发
     */

    // 👇该注解标记的接口不需要登录
    @SaIgnore
    @ApiOperation(value = "（演示接口；可删除）性别下拉框")
    @GetMapping(value = "/select-options/genders")
    public ApiResult<List<SelectOptionItemVO>> genders(YesOrNoEnum demo) {
        if (demo == YesOrNoEnum.YES) {
            // demo=YES时，不输出「未知」、多输出demoString1
            List<SelectOptionItemVO> ret = SelectOptionItemVO.listOf(GenderEnum.class, item -> item != GenderEnum.UNKNOWN);
            for (SelectOptionItemVO item : ret) {
                item.setDemoString1("文本字段1-" + item.getValue());
            }
            return ApiResult.data(ret);
        }
        // 默认返回
        return ApiResult.data(SelectOptionItemVO.listOf(GenderEnum.class));
    }

    // 👇该注解标记的接口不需要登录
    @SaIgnore
    @ApiOperation(value = "（演示接口；可删除）部门下拉框")
    @GetMapping(value = "/select-options/depts")
    public ApiResult<List<SelectOptionItemVO>> depts(YesOrNoEnum demo) {
        if (demo == YesOrNoEnum.YES) {
            // demo=YES时，多输出上级ID、多输出demoString1
            List<SelectOptionItemVO> ret = SelectOptionItemVO.listOf(sysDeptService.adminList(), SysDeptBO::getId, SysDeptBO::getTitle, SysDeptBO::getParentId);
            for (SelectOptionItemVO item : ret) {
                item.setDemoString1("文本字段1-" + item.getId());
            }
            return ApiResult.data(ret);
        }
        // 默认返回
        return ApiResult.data(SelectOptionItemVO.listOf(sysDeptService.adminList(), SysDeptBO::getId, SysDeptBO::getTitle));
    }

}
