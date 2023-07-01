package cc.uncarbon.module.sys.extension;

import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.model.request.AdminInsertSysLogDTO;
import cc.uncarbon.module.sys.model.response.IPLocationBO;
import org.aspectj.lang.JoinPoint;

/**
 * SysLog 切面实现类扩展
 *
 */
public interface SysLogAspectExtension {

    /**
     * 查询IP地址属地
     * @param ip IPv4或IPv6地址
     */
    default IPLocationBO queryIPLocation(String ip) {
        return IPLocationBO.unknown();
    }

    /**
     * 保存到 DB 前
     * @param insertSysLogDTO 新系统日志DTO
     * @param joinPoint 切点
     * @param annotation 注解实例
     * @param e 异常实例；如果没有发生异常则值为null
     * @param ret 原始方法过程，于顺利运行时的返回值
     */
    default void beforeSaving(final AdminInsertSysLogDTO insertSysLogDTO, final JoinPoint joinPoint, SysLog annotation, final Throwable e, Object ret) {

    }

}
