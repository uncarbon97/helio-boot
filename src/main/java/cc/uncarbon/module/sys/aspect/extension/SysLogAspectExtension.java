package cc.uncarbon.module.sys.aspect.extension;

import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.model.request.AdminInsertSysLogDTO;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * SysLog 切面实现类扩展
 *
 * @author Uncarbon
 */
public interface SysLogAspectExtension {

    /**
     * 保存到 DB 前
     * @param sysLogAnnotation 注解
     * @param point 切面 point
     * @param insertSysLogDTO 日志请求体
     */
    void beforeSaving(SysLog sysLogAnnotation, ProceedingJoinPoint point, AdminInsertSysLogDTO insertSysLogDTO);

}
