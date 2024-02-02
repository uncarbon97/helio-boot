package cc.uncarbon.module.sys.service;

import cc.uncarbon.framework.core.constant.HelioConstant;
import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.core.enums.EnabledStatusEnum;
import cc.uncarbon.framework.core.exception.BusinessException;
import cc.uncarbon.framework.core.function.StreamFunction;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.entity.SysMenuEntity;
import cc.uncarbon.module.sys.enums.SysErrorEnum;
import cc.uncarbon.module.sys.enums.SysMenuTypeEnum;
import cc.uncarbon.module.sys.mapper.SysMenuMapper;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysMenuDTO;
import cc.uncarbon.module.sys.model.response.SysMenuBO;
import cc.uncarbon.module.sys.model.response.VbenAdminMenuMetaVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 后台菜单
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysMenuService {

    private final SysMenuMapper sysMenuMapper;
    private final SysRoleMenuRelationService sysRoleMenuRelationService;

    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(0L, 0L);


    /**
     * 后台管理-列表
     */
    public List<SysMenuBO> adminList() {
        List<SysMenuEntity> entityList = sysMenuMapper.selectList(
                new QueryWrapper<SysMenuEntity>()
                        .lambda()
                        // 排序
                        .orderByAsc(SysMenuEntity::getSort)
        );

        return this.entityList2BOs(entityList);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id 主键ID
     * @return null or BO
     */
    public SysMenuBO getOneById(Long id) {
        return this.getOneById(id, false);
    }

    /**
     * 根据 ID 取详情
     *
     * @param id               主键ID
     * @param throwIfInvalidId 是否在 ID 无效时抛出异常
     * @return null or BO
     */
    public SysMenuBO getOneById(Long id, boolean throwIfInvalidId) throws BusinessException {
        SysMenuEntity entity = sysMenuMapper.selectById(id);
        if (throwIfInvalidId) {
            SysErrorEnum.INVALID_ID.assertNotNull(entity);
        }

        return this.entity2BO(entity);
    }

    /**
     * 后台管理-新增
     *
     * @return 主键ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertOrUpdateSysMenuDTO dto) {
        log.info("[后台管理-新增后台菜单] >> 入参={}", dto);
        this.checkExistence(dto);

        if (ObjectUtil.isNull(dto.getParentId())) {
            dto.setParentId(0L);
        }

        dto.setId(null);

        SysMenuEntity entity = new SysMenuEntity();
        BeanUtil.copyProperties(dto, entity);

        sysMenuMapper.insert(entity);

        return entity.getId();
    }

    /**
     * 后台管理-编辑
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdate(AdminInsertOrUpdateSysMenuDTO dto) {
        log.info("[后台管理-编辑后台菜单] >> 入参={}", dto);
        this.checkExistence(dto);

        if (ObjectUtil.isNull(dto.getParentId())) {
            dto.setParentId(0L);
        }

        SysMenuEntity entity = new SysMenuEntity();
        BeanUtil.copyProperties(dto, entity);

        sysMenuMapper.updateById(entity);
    }

    /**
     * 后台管理-删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void adminDelete(Collection<Long> ids) {
        log.info("[后台管理-删除后台菜单] >> 入参={}", ids);
        sysMenuMapper.deleteBatchIds(ids);
    }

    /**
     * 后台管理-取侧边菜单
     */
    public List<SysMenuBO> adminListSideMenu() {
        Set<Long> visibleMenuIds = this.listCurrentUserVisibleMenuIds();
        return this.listByIds(visibleMenuIds, SysMenuTypeEnum.forAdminSide());
    }

    /**
     * 后台管理-取所有可见菜单
     */
    public List<SysMenuBO> adminListVisibleMenu() {
        Set<Long> visibleMenuIds = this.listCurrentUserVisibleMenuIds();
        return this.listByIds(visibleMenuIds, SysMenuTypeEnum.all());
    }

    /**
     * 根据角色Ids，获取角色ID 对应的权限名 Map
     *
     * @return map key=角色ID value=权限名集合
     */
    public Map<Long, Set<String>> getRoleIdPermissionMap(Collection<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return Collections.emptyMap();
        }

        HashMap<Long, Set<String>> ret = new HashMap<>(roleIds.size(), 1);

        roleIds.forEach(
                roleId -> {
                    Set<String> permissions;

                    if (SysConstant.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
                        // 超级管理员读取所有权限，不管有没有被禁用
                        permissions = sysMenuMapper.selectList(null).stream()
                                .map(SysMenuEntity::getPermission)
                                .filter(StrUtil::isNotEmpty)
                                .collect(Collectors.toSet());
                    } else {
                        // 非超级管理员则通过角色ID，关联查询拥有的菜单，菜单上有权限名

                        Set<Long> menuIds = sysRoleMenuRelationService.listMenuIdsByRoleIds(Collections.singleton(roleId));
                        if (CollUtil.isEmpty(menuIds)) {
                            permissions = Collections.emptySet();
                        } else {
                            permissions = sysMenuMapper.selectList(
                                            new QueryWrapper<SysMenuEntity>()
                                                    .lambda()
                                                    .select(SysMenuEntity::getPermission)
                                                    .in(SysMenuEntity::getId, menuIds)
                                                    .eq(SysMenuEntity::getStatus, EnabledStatusEnum.ENABLED)
                                    )
                                    .stream()
                                    .map(SysMenuEntity::getPermission)
                                    .filter(StrUtil::isNotEmpty)
                                    .collect(Collectors.toSet());
                        }
                    }

                    ret.put(roleId, permissions);
                }
        );

        return ret;
    }

    /**
     * 根据菜单ID集合，取权限名集合
     */
    public Set<String> listPermissionsByMenuIds(Collection<Long> menuIds) {
        if (CollUtil.isEmpty(menuIds)) {
            return Collections.emptySet();
        }

        return sysMenuMapper.selectList(
                new QueryWrapper<SysMenuEntity>()
                        .lambda()
                        .select(SysMenuEntity::getPermission)
                        .in(SysMenuEntity::getId, menuIds)
        ).stream().map(SysMenuEntity::getPermission).filter(StrUtil::isNotEmpty).collect(Collectors.toSet());
    }


    /*
    ----------------------------------------------------------------
                        私有方法 private methods
    ----------------------------------------------------------------
     */

    /**
     * 实体转 BO
     *
     * @param entity 实体
     * @return BO
     */
    private SysMenuBO entity2BO(SysMenuEntity entity) {
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
                .setName(snowflakeIdStr)
                .setMeta(new VbenAdminMenuMetaVO(bo.getTitle(), false, bo.getIcon()));

        // 这里是兼容 JDK8 的写法，使用较高 JDK 版本可使用语法糖
        switch (bo.getType()) {
            case DIR:
            case BUTTON:
                bo
                        .setComponent(SysConstant.VBEN_ADMIN_BLANK_VIEW)
                        .setExternalLink(null)
                        .setPath(StrPool.SLASH + snowflakeIdStr);
                break;
            case MENU:
                bo
                        .setExternalLink(null)
                        .setPath(bo.getComponent());
                // 防止用户忘记加了, 主动补充/
                if (CharSequenceUtil.isNotBlank(bo.getPath()) && !bo.getPath().startsWith(StrPool.SLASH)) {
                    bo.setPath(StrPool.SLASH + bo.getPath());
                }
                break;
            case EXTERNAL_LINK:
                bo
                        .setComponent(bo.getExternalLink())
                        .setPath(bo.getExternalLink());
                break;
            default: break;
        }
        return bo;
    }

    private List<SysMenuBO> entityList2BOs(List<SysMenuEntity> entityList) {
        // 深拷贝
        List<SysMenuBO> ret = new ArrayList<>(entityList.size());
        entityList.forEach(
                entity -> ret.add(this.entity2BO(entity))
        );

        return ret;
    }

    /**
     * 取当前账号可见菜单Ids
     *
     * @return 菜单Ids
     */
    private Set<Long> listCurrentUserVisibleMenuIds() {
        // 1. 取当前账号拥有角色Ids
        Set<Long> roleIds = UserContextHolder.getUserContext().getRolesIds();
        SysErrorEnum.NO_ROLE_AVAILABLE_FOR_CURRENT_USER.assertNotEmpty(roleIds);

        // 2. 得到所有可用的 菜单ID-上级菜单ID map，备用
        Map<Long, Long> allMenuMap = sysMenuMapper.selectList(
                new QueryWrapper<SysMenuEntity>()
                        .lambda()
                        .select(SysMenuEntity::getId, SysMenuEntity::getParentId)
                        .eq(SysMenuEntity::getStatus, EnabledStatusEnum.ENABLED)
        ).stream().collect(Collectors.toMap(SysMenuEntity::getId, SysMenuEntity::getParentId, StreamFunction.ignoredThrowingMerger()));

        // 3. 超级管理员直接返回所有菜单
        if (roleIds.contains(SysConstant.SUPER_ADMIN_ROLE_ID)) {
            return new HashSet<>(allMenuMap.keySet());
        }

        // 4. 根据现有角色，获取直接关联的菜单ID
        Set<Long> directlyRelatedMenuIds = sysRoleMenuRelationService.listMenuIdsByRoleIds(roleIds);
        SysErrorEnum.NO_MENU_AVAILABLE_FOR_CURRENT_ROLE.assertNotEmpty(directlyRelatedMenuIds);

        // 5. 因为直接关联的菜单ID，可能不包含父级菜单，使得级联关系缺失，这里得给他补上
        return this.traceParentMenuIds(allMenuMap, directlyRelatedMenuIds);
    }

    private List<SysMenuBO> listByIds(Collection<Long> ids, List<SysMenuTypeEnum> types)
            throws IllegalArgumentException {
        Assert.notEmpty(ids);
        Assert.notEmpty(types);

        List<SysMenuEntity> entityList = sysMenuMapper.selectList(
                new QueryWrapper<SysMenuEntity>()
                        .lambda()
                        .in(SysMenuEntity::getId, ids)
                        .in(SysMenuEntity::getType, types)
                        .orderByAsc(SysMenuEntity::getSort)
        );

        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }

        return this.entityList2BOs(entityList);
    }

    /**
     * 检查是否已存在相同数据
     *
     * @param dto DTO
     */
    private void checkExistence(AdminInsertOrUpdateSysMenuDTO dto) {
        if (CharSequenceUtil.isNotBlank(dto.getPermission())) {
            dto.setPermission(CharSequenceUtil.cleanBlank(dto.getPermission()));

            SysMenuEntity existingEntity = sysMenuMapper.selectOne(
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
     * 追溯并补充可能缺失的级联上级菜单ID
     *
     * @param allMenuMap 完整的 菜单ID-上级菜单ID map
     * @param directlyRelatedMenuIds 当前用户直接关联的菜单ID集合
     * @return Set<Long> 已经补充好的、完整的菜单ID集合
     */
    private Set<Long> traceParentMenuIds(Map<Long, Long> allMenuMap, Collection<Long> directlyRelatedMenuIds) {
        // 返回值
        Set<Long> ret = new HashSet<>(directlyRelatedMenuIds.size() << 1);
        ret.addAll(directlyRelatedMenuIds);

        // 本次循环要遍历的菜单ID集合
        HashSet<Long> thisLoop;
        // 下次循环要遍历的菜单ID集合
        HashSet<Long> nextLoop = new HashSet<>(directlyRelatedMenuIds);

        do {
            // 深拷贝数据，用于本次循环
            thisLoop = new HashSet<>(nextLoop);
            nextLoop.clear();

            for (Long menuId : thisLoop) {
                Long parentId = allMenuMap.get(menuId);

                if (Objects.nonNull(parentId)) {
                    // 上级菜单可能还会有祖级菜单，需要继续上溯
                    nextLoop.add(parentId);
                }
            }

            ret.addAll(nextLoop);

        } while (!nextLoop.isEmpty());

        return ret;
    }
}
