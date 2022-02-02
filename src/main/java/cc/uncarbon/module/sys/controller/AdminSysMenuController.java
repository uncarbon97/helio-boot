package cc.uncarbon.module.sys.controller;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.web.model.request.IdsDTO;
import cc.uncarbon.framework.web.model.response.ApiResult;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysMenuDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysMenuDTO;
import cc.uncarbon.module.sys.model.response.SysMenuBO;
import cc.uncarbon.module.sys.service.SysMenuService;
import cc.uncarbon.module.sys.service.SysParamService;
import cc.uncarbon.module.sys.util.AdminStpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author Uncarbon
 */
@RequiredArgsConstructor
@SaCheckLogin(type = AdminStpUtil.TYPE)
@Slf4j
@Api(value = "后台菜单管理接口", tags = {"后台菜单管理接口"})
@RequestMapping(SysConstant.SYS_MODULE_CONTEXT_PATH + HelioConstant.Version.HTTP_API_VERSION_V1 + "/sys/menus")
@RestController
public class AdminSysMenuController {

    private static final String PERMISSION_PREFIX = "SysMenu:";

    private final SysMenuService sysMenuService;

    private final SysParamService sysParamService;

    private final StringRedisTemplate stringRedisTemplate;


    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping
    public ApiResult<List<SysMenuBO>> list(AdminListSysMenuDTO dto) {
        return ApiResult.data(sysMenuService.adminList(dto));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.RETRIEVE)
    @ApiOperation(value = "详情", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/{id}")
    public ApiResult<SysMenuBO> getById(@PathVariable Long id) {
        return ApiResult.data(sysMenuService.getOneById(id, true));
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.CREATE)
    @ApiOperation(value = "新增", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping
    public ApiResult<?> insert(@RequestBody @Valid AdminInsertOrUpdateSysMenuDTO dto) {
        sysMenuService.adminInsert(dto);
        sysMenuService.cleanMenuCacheInRedis();

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.UPDATE)
    @ApiOperation(value = "编辑", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PutMapping(value = "/{id}")
    public ApiResult<?> update(@PathVariable Long id, @RequestBody @Valid AdminInsertOrUpdateSysMenuDTO dto) {
        dto.setId(id);
        sysMenuService.adminUpdate(dto);
        sysMenuService.cleanMenuCacheInRedis();

        return ApiResult.success();
    }

    @SaCheckPermission(type = AdminStpUtil.TYPE, value = PERMISSION_PREFIX + HelioConstant.Permission.DELETE)
    @ApiOperation(value = "删除", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @DeleteMapping
    public ApiResult<?> delete(@RequestBody @Valid IdsDTO<Long> dto) {
        sysMenuService.adminDelete(dto.getIds());
        sysMenuService.cleanMenuCacheInRedis();

        return ApiResult.success();
    }

    @ApiOperation(value = "取当前账号可见侧边菜单", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/side")
    public ApiResult<List<SysMenuBO>> adminListSideMenu() {
        String redisKey = String.format(SysConstant.REDIS_KEY_SIDE_MENU_BY_USERID, UserContextHolder.getUserContext().getUserId());
        Object redisValue = stringRedisTemplate.opsForValue().get(redisKey);

        if (redisValue == null) {
            redisValue = sysMenuService.adminListSideMenu();

            // 记录到缓存
            String sysMenuCacheDuration = sysParamService.getParamValueByName(SysConstant.PARAM_KEY_CACHE_MENU_DURATION, "30");
            stringRedisTemplate.opsForValue().set(redisKey, JSONUtil.toJsonStr(redisValue), Integer.parseInt(sysMenuCacheDuration), TimeUnit.MINUTES);
        } else {
            redisValue = JSONUtil.parse(redisValue).toBean(ArrayList.class);
        }

        return ApiResult.data((List<SysMenuBO>) redisValue);
    }

    @ApiOperation(value = "取当前账号所有可见菜单", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/all")
    public ApiResult<List<SysMenuBO>> adminListVisibleMenu() {
        String redisKey = String.format(SysConstant.REDIS_KEY_VISIBLE_MENU_BY_USERID, UserContextHolder.getUserContext().getUserId());
        Object redisValue = stringRedisTemplate.opsForValue().get(redisKey);

        if (redisValue == null) {
            redisValue = sysMenuService.adminListVisibleMenu();

            // 记录到缓存
            String sysMenuCacheDuration = sysParamService.getParamValueByName(SysConstant.PARAM_KEY_CACHE_MENU_DURATION, "30");
            stringRedisTemplate.opsForValue().set(redisKey, JSONUtil.toJsonStr(redisValue), Integer.parseInt(sysMenuCacheDuration), TimeUnit.MINUTES);
        } else {
            redisValue = JSONUtil.parse(redisValue).toBean(ArrayList.class);
        }

        return ApiResult.data((List<SysMenuBO>) redisValue);
    }

}
