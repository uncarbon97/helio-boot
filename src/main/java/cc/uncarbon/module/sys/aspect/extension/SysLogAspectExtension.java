package cc.uncarbon.module.sys.aspect.extension;

import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.entity.SysLogEntity;
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
     * @param sysLogEntity 日志实体对象
     */
    void beforeSaving(SysLog sysLogAnnotation, ProceedingJoinPoint point, SysLogEntity sysLogEntity);

}
