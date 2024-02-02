package cc.uncarbon.module.sys.facade.impl;

import cc.uncarbon.module.sys.entity.SysTenantEntity;
import cc.uncarbon.module.sys.facade.SysTenantFacade;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysRoleDTO;
import cc.uncarbon.module.sys.model.request.AdminInsertOrUpdateSysUserDTO;
import cc.uncarbon.module.sys.model.request.AdminInsertSysTenantDTO;
import cc.uncarbon.module.sys.service.SysRoleService;
import cc.uncarbon.module.sys.service.SysTenantService;
import cc.uncarbon.module.sys.service.SysUserRoleRelationService;
import cc.uncarbon.module.sys.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统租户解耦层，用于解决循环依赖
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class SysTenantFacadeImpl implements SysTenantFacade {

    private final SysTenantService sysTenantService;
    private final SysRoleService sysRoleService;
    private final SysUserRoleRelationService sysUserRoleRelationService;
    private final SysUserService sysUserService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long adminInsert(AdminInsertSysTenantDTO dto) {
        log.info("[后台管理-新增系统租户] >> 入参={}", dto);
        sysTenantService.checkExistence(dto);

        /*
        1. 加入一个新租户(tenant)
        这里是直接顺带创建管理员账号了, 你可以根据业务需要决定是否创建
         */
        SysTenantEntity entity = sysTenantService.adminInsert(dto);

        Long newTenantEntityId = entity.getId();
        Long newTenantId = entity.getTenantId();

        /*
        2. 创建一个新角色(role)
        注意: 这里并没有指派其可见菜单，需要用超管账号授权
         */
        Long newRoleId = sysRoleService.adminInsert(
                AdminInsertOrUpdateSysRoleDTO.builder()
                        .tenantId(newTenantId)
                        .title(dto.getTenantName() + "管理员")
                        .value("Admin")
                        .build()
        );

        // 3. 创建一个新用户
        Long newUserId = sysUserService.adminInsert(
                AdminInsertOrUpdateSysUserDTO.builder()
                        .tenantId(newTenantId)
                        .username(dto.getTenantAdminUsername())
                        .passwordOfNewUser(dto.getTenantAdminPassword())
                        .nickname(dto.getTenantName() + "管理员")
                        .email(dto.getTenantAdminEmail())
                        .phoneNo(dto.getTenantAdminPhoneNo())
                        .build()
        );

        // 4. 将新用户绑定至新角色上
        sysUserRoleRelationService.adminInsert(newTenantId, newUserId, newRoleId);

        // 5. 把管理员账号更新进库
        SysTenantEntity update = new SysTenantEntity().setTenantAdminUserId(newUserId);
        update.setId(newTenantEntityId);
        sysTenantService.adminUpdate(update);
        return newTenantId;
    }

}
