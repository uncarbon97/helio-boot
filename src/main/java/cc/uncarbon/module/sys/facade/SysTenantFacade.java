package cc.uncarbon.module.sys.facade;

import cc.uncarbon.module.sys.model.request.AdminInsertSysTenantDTO;

/**
 * 系统租户解耦层，用于解决循环依赖
 */
public interface SysTenantFacade {

    /**
     * 后台管理-新增
     */
    Long adminInsert(AdminInsertSysTenantDTO dto);

}
