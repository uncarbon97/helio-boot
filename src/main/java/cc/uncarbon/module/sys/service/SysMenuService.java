package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.crud.service.impl.HelioBaseServiceImpl;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.entity.SysMenuEntity;
import cc.uncarbon.module.sys.enums.GenericStatusEnum;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.enums.SysMenuTypeEnum;
import cc.uncarbon.module.sys.mapper.SysMenuMapper;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysMenuDTO;
import cc.uncarbon.module.sys.model.request.AdminListSysMenuDTO;
import cc.uncarbon.module.sys.model.response.SysMenuBO;
import cc.uncarbon.module.sys.model.response.VbenAdminMenuMetaBO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 后台菜单
 * @author Uncarbon
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysMenuService extends HelioBaseServiceImpl<SysMenuMapper, SysMenuEntity> {

    private final SysRoleMenuRelationService sysRoleMenuRelationService;

    private final SysUserRoleRelationService sysUserRoleRelationService;

    private final StringRedisTemplate stringRedisTemplate;

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(0L, 0L);


    /**
     * 后台管理-列表
     */
    public List<SysMenuBO> adminList(AdminListSysMenuDTO dto) {
        List<SysMenuEntity> entityList = this.list(
                new QueryWrapper<SysMenuEntity>()
                        .lambda()
                        // 标题
                        .like(StrUtil.isNotBlank(dto.getTitle()), SysMenuEntity::getTitle, StrUtil.cleanBlank(dto.getTitle()))
                        // 上级ID
                        .eq(SysMenuEntity::getParentId, ObjectUtil.isNull(dto.getParentId()) ? SysConstant.ROOT_PARENT_ID : dto.getParentId())
                        // 菜单类型
                        .in(CollUtil.isNotEmpty(dto.getMenuTypes()), SysMenuEntity::getType, dto.getMenuTypes())
                        // 排序
                        .orderByAsc(SysMenuEntity::getSort)
        );

        return this.entityList2BOs(entityList, true);
    }

    /**
     * 通用-详情
     *
     * @deprecated 使用 getOneById(java.lang.Long, boolean) 替代
     */
    @Deprecated
    public SysMenuBO getOneById(Long entityId) throws BusinessException {
        return this.getOneById(entityId, true);
    }

    /**
     * 通用-详情
     *
     * @param entityId 实体类主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public SysMenuBO getOneById(Long entityId, boolean throwIfInvalidId) throws BusinessException {
        SysMenuEntity entity = this.getById(entityId);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity, false, null);
    }

    /**
     * 后台管理-新增
     * @return 主键ID
     */
    @SysLog(value = "新增后台菜单")
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysMenuDTO dto) {
        log.info("[后台管理-新增后台菜单] >> DTO={}", dto);
        this.checkExistence(dto);

        if (ObjectUtil.isNull(dto.getParentId())) {
            dto.setParentId(0L);
        }

        dto.setId(null);

        SysMenuEntity entity = new SysMenuEntity();
        BeanUtil.copyProperties(dto, entity);

        this.save(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @SysLog(value = "编辑后台菜单")
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateSysMenuDTO dto) {
        log.info("[后台管理-编辑后台菜单] >> DTO={}", dto);
        this.checkExistence(dto);

        if (ObjectUtil.isNull(dto.getParentId())) {
            dto.setParentId(0L);
        }

        SysMenuEntity entity = new SysMenuEntity();
        BeanUtil.copyProperties(dto, entity);

        this.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @SysLog(value = "删除后台菜单")
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除后台菜单] >> ids={}", ids);
        this.removeByIds(ids);
    }

    /**
     * 后台管理-取当前账号可见侧边菜单
     */
    public List<SysMenuBO> adminListSideMenu() {
        Set<Long> visibleMenuIds = this.listCurrentUserVisibleMenuId();

        // 3. 取出无上级节点菜单
        List<SysMenuTypeEnum> requiredMenuTypes = CollUtil.newArrayList(SysMenuTypeEnum.DIR, SysMenuTypeEnum.MENU, SysMenuTypeEnum.EXTERNAL_LINK);
        List<SysMenuBO> allMenus = this.listMenuByParentId(visibleMenuIds, requiredMenuTypes, SysConstant.ROOT_PARENT_ID);
        log.debug("[后台管理][取当前账号可见侧边菜单] 取出无上级节点菜单 >> {}", allMenus);

        // 4.递归查询子节点, 孙节点, 曾孙节点...
        this.recursiveFindChildren(visibleMenuIds, requiredMenuTypes, allMenus);
        log.debug("[后台管理][取当前账号可见侧边菜单] 递归查询子节点, 孙节点, 曾孙节点... >> {}", allMenus);

        return allMenus;
    }

    /**
     * 后台管理-取当前账号所有可见菜单(包括按钮类型)
     */
    public List<SysMenuBO> adminListVisibleMenu() {
        Set<Long> visibleMenuIds = this.listCurrentUserVisibleMenuId();

        // 3. 取出无上级节点菜单
        List<SysMenuTypeEnum> requiredMenuTypes = CollUtil.newArrayList(SysMenuTypeEnum.DIR, SysMenuTypeEnum.MENU, SysMenuTypeEnum.EXTERNAL_LINK, SysMenuTypeEnum.BUTTON);
        List<SysMenuBO> allMenus = this.listMenuByParentId(visibleMenuIds, requiredMenuTypes, SysConstant.ROOT_PARENT_ID);
        log.debug("[后台管理][取当前账号所有可见菜单] 取出无上级节点菜单:{}", allMenus);

        // 4.递归查询子节点, 孙节点, 曾孙节点...
        this.recursiveFindChildren(visibleMenuIds, requiredMenuTypes, allMenus);
        log.debug("[后台管理][取当前账号所有可见菜单] 递归查询子节点, 孙节点, 曾孙节点...:{}", allMenus);

        return allMenus;
    }

    /**
     * 通用-清除Redis中所有菜单缓存
     */
    public void cleanMenuCacheInRedis() {
        this.cleanMenuCacheInRedis(null);
    }

    /**
     * 通用-根据用户ID，清除Redis中相关菜单缓存
     * @param userId 用户ID；为null则清除所有
     */
    public void cleanMenuCacheInRedis(Long userId) {
        String redisKey;
        if (ObjectUtil.isNull(userId)) {
            redisKey = "*";
        } else {
            redisKey = "userId_" + StrUtil.toString(userId);
        }

        log.info("[清除Redis中相关菜单缓存] >> redisKey={}", redisKey);

        Set<String> keys = stringRedisTemplate.keys(SysConstant.REDIS_KEY_SIDE_MENU + redisKey);
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }

        keys = stringRedisTemplate.keys(SysConstant.REDIS_KEY_VISIBLE_MENU + redisKey);
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }

    /**
     * 根据角色Ids取权限串List
     */
    public Set<String> listPermissionByRoleIds(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptySet();
        }



        // 超级管理员直接允许所有权限
        if (roleIds.contains(SysConstant.SUPER_ADMIN_ROLE_ID)) {
            return this.list().stream().map(SysMenuEntity::getPermission).filter(StrUtil::isNotEmpty).collect(Collectors.toSet());
        }

        // 非超级管理员则通过角色ID，关联查询拥有的菜单
        Set<Long> menuIds = sysRoleMenuRelationService.listMenuIdByRoleIds(roleIds);
        // 过滤实际已被禁用的菜单
        menuIds = this.filterDisabledIds(menuIds);

        if (CollUtil.isEmpty(menuIds)) {
            return Collections.emptySet();
        }

        return this.list(
                new QueryWrapper<SysMenuEntity>()
                        .lambda()
                        .select(SysMenuEntity::getPermission)
                        .in(SysMenuEntity::getId, menuIds)
        ).stream().map(SysMenuEntity::getPermission).filter(StrUtil::isNotEmpty).collect(Collectors.toSet());
    }


    /*
    私有方法
    ------------------------------------------------------------------------------------------------
     */

    private SysMenuBO entity2BO(SysMenuEntity entity, boolean traverseChildren, List<SysMenuTypeEnum> menuTypes) {
        if (entity == null) {
            return null;
        }

        SysMenuBO bo = new SysMenuBO();
        BeanUtil.copyProperties(entity, bo);

        // 可以在此处为BO填充字段
        if (SysConstant.ROOT_PARENT_ID.equals(bo.getParentId())) {
            bo.setParentId(null);
        }

        String snowflakeIdStr = SNOWFLAKE.nextIdStr();
        bo
                .setIdStr(StrUtil.toString(bo.getId()))
                .setName(snowflakeIdStr)
                .setMeta(
                        VbenAdminMenuMetaBO.builder()
                                .title(bo.getTitle())
                                .affix(false)
                                .icon(bo.getIcon())
                                .build()
                );

        // 这里是兼容 JDK8 的写法，使用较高 JDK 版本可使用语法糖
        switch (bo.getType()) {
            case DIR:
            case BUTTON: {
                bo
                        .setComponent(SysConstant.VBEN_ADMIN_BLANK_VIEW)
                        .setExternalLink(null)
                        .setPath("/" + snowflakeIdStr)
                ;
                break;
            }
            case MENU: {
                bo
                        .setExternalLink(null)
                        .setPath(bo.getComponent())
                ;
                // 防止用户忘记加了, 主动补充/
                if (StrUtil.isNotBlank(bo.getPath()) && !bo.getPath().startsWith("/")) {
                    bo.setPath("/" + bo.getPath());
                }
                break;
            }
            case EXTERNAL_LINK: {
                bo
                        .setComponent(bo.getExternalLink())
                        .setPath(bo.getExternalLink())
                ;
                break;
            }
        }

        // 遍历子级菜单
        if (traverseChildren) {
            List<SysMenuBO> children = this.adminList(
                    AdminListSysMenuDTO.builder()
                            .parentId(bo.getId())
                            .menuTypes(menuTypes)
                            .build()
            );
            if (CollUtil.isEmpty(children)) {
                children = null;
            }

            bo.setChildren(children);
        }

        return bo;
    }

    private List<SysMenuBO> entityList2BOs(List<SysMenuEntity> entityList, Boolean traverseChildren) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        // 深拷贝
        List<SysMenuBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity, traverseChildren, null))
        );

        return ret;
    }

    /**
     * 取当前账号可见菜单Ids
     * @return 菜单Ids
     */
    private Set<Long> listCurrentUserVisibleMenuId() {
        // 1. 取当前账号拥有角色Ids
        Collection<Long> roleIds = UserContextHolder.getUserContext().getRolesIds();
        log.debug("[后台管理][取当前账号可见菜单Ids] 当前账号拥有角色Ids >> {}", roleIds);
        if (CollUtil.isEmpty(roleIds)) {
            throw new BusinessException(SysErrorEnum.NO_ROLE_AVAILABLE_FOR_CURRENT_USER);
        }

        // 2-1. 超级管理员直接允许所有菜单，即使是禁用状态
        if (roleIds.contains(SysConstant.SUPER_ADMIN_ROLE_ID)) {
            return this.list(
                    new QueryWrapper<SysMenuEntity>()
                            .lambda()
                            .select(SysMenuEntity::getId)
            ).stream().map(SysMenuEntity::getId).collect(Collectors.toSet());
        }

        // 2-2. 根据角色Ids取菜单Ids
        Set<Long> menuIds = sysRoleMenuRelationService.listMenuIdByRoleIds(roleIds);
        log.debug("[后台管理][取当前账号可见菜单Ids] 根据角色Ids取菜单Ids >> {}", menuIds);
        if (CollUtil.isEmpty(menuIds)) {
            throw new BusinessException(SysErrorEnum.NO_MENU_AVAILABLE_FOR_CURRENT_ROLE);
        }

        /*
        3. 补充上级菜单ID
        这个Ant-Design的Tree组件有个吊诡的地方, 只选一部分子级的话, 父级菜单不算勾选
        只能在代码里补上了
         */
        List<Long> missingParentIds = this.supplementMissingParentIds(menuIds);
        menuIds.addAll(missingParentIds);

        return menuIds;
    }

    /**
     * 补充遗漏的上级菜单ID
     */
    private List<Long> supplementMissingParentIds(Set<Long> menuIds) {
        // 返回值
        List<Long> ret = new ArrayList<>(128);
        // 单次循环返回值
        Set<Long> loopRet = menuIds;

        for (int i = 0; i < 5; i++) {
            // 最多上溯5层
            loopRet = this.list(
                    new QueryWrapper<SysMenuEntity>()
                            .lambda()
                            .select(SysMenuEntity::getParentId)
                            .ne(SysMenuEntity::getParentId, SysConstant.ROOT_PARENT_ID)
                            .in(SysMenuEntity::getId, loopRet)
                            .notIn(SysMenuEntity::getParentId, loopRet)
            ).stream().map(SysMenuEntity::getParentId).collect(Collectors.toSet());

            if (loopRet.isEmpty()) {
                break;
            } else {
                ret.addAll(loopRet);
            }
        }

        return ret;
    }

    private List<SysMenuBO> listMenuByParentId(Collection<Long> visibleMenuIds, List<SysMenuTypeEnum> requiredMenuTypes, Long parentId) throws IllegalArgumentException {
        if (CollUtil.isEmpty(visibleMenuIds)) {
            throw new IllegalArgumentException("visibleMenuIds不能为空");
        }

        if (CollUtil.isEmpty(requiredMenuTypes)) {
            throw new IllegalArgumentException("requiredMenuTypes不能为空");
        }

        List<SysMenuEntity> entityList = this.list(
                new QueryWrapper<SysMenuEntity>()
                        .lambda()
                        .in(SysMenuEntity::getId, visibleMenuIds)
                        .in(SysMenuEntity::getType, requiredMenuTypes)
                        .eq(SysMenuEntity::getParentId, parentId)
                        .orderByAsc(SysMenuEntity::getSort)
        );

        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        List<SysMenuBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                each -> ret.add(this.entity2BO(each, false, requiredMenuTypes))
        );

        return ret;
    }

    /**
     * 递归查询子节点
     */
    private void recursiveFindChildren(Collection<Long> visibleMenuIds, List<SysMenuTypeEnum> requiredMenuTypes, List<SysMenuBO> menuBOs) {
        menuBOs.forEach(
                menu -> {
                    // 查询子节点
                    List<SysMenuBO> itsChildren = this.listMenuByParentId(visibleMenuIds, requiredMenuTypes, menu.getId());
                    if (CollUtil.isNotEmpty(itsChildren)) {
                        menu.setChildren(itsChildren);
                        // 递归查询孙节点
                        this.recursiveFindChildren(visibleMenuIds, requiredMenuTypes, menu.getChildren());
                    }
                }
        );
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminInsertOrUpdateSysMenuDTO dto) {
        if (StrUtil.isNotBlank(dto.getPermission())) {
            dto.setPermission(StrUtil.cleanBlank(dto.getPermission()));

            SysMenuEntity existingEntity = this.getOne(
                    new QueryWrapper<SysMenuEntity>()
                            .lambda()
                            // 仅取主键ID
                            .select(SysMenuEntity::getId)
                            // 权限标识相同
                            .eq(SysMenuEntity::getPermission, dto.getPermission())
                            .last(HelioConstant.CRUD.SQL_LIMIT_1)
            );

            if (existingEntity != null && !existingEntity.getId().equals(dto.getId())) {
                throw new BusinessException(400, "已存在相同权限标识，请重新输入");
            }
        }
    }

    /**
     * 内部-过滤被禁用的菜单ID
     *
     * @param visibleMenuIds 待过滤的可见的菜单ID列表
     * @return 过滤后的菜单ID列表
     */
    private Set<Long> filterDisabledIds(Set<Long> visibleMenuIds) {
        if (CollUtil.isEmpty(visibleMenuIds)) {
            return Collections.emptySet();
        }

        /*
        1. 找出目前被禁用的菜单ID
         */
        List<Long> disabledMenuIds = this.list(
                new QueryWrapper<SysMenuEntity>()
                        .lambda()
                        .select(SysMenuEntity::getId)
                        .eq(SysMenuEntity::getStatus, GenericStatusEnum.DISABLED)
        ).stream().map(SysMenuEntity::getId).collect(Collectors.toList());


        /*
        2. 提取出符合以下条件的菜单ID:
            启用状态 && 父菜单未被禁用
         */
        return this.list(
                new QueryWrapper<SysMenuEntity>()
                        .lambda()
                        .select(SysMenuEntity::getId)
                        .and(
                                wrapper -> wrapper
                                        .eq(SysMenuEntity::getStatus, GenericStatusEnum.ENABLED)
                                        .notIn(CollUtil.isNotEmpty(disabledMenuIds), SysMenuEntity::getParentId, disabledMenuIds)
                        )
                        .in(SysMenuEntity::getId, visibleMenuIds)
        ).stream().map(SysMenuEntity::getId).collect(Collectors.toSet());
    }
}
