package cc.uncarbon.module.sys.aspect.extension;

import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.entity.SysLogEntity;
import cc.uncarbon.module.sys.model.request.SysUserLoginDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

/**
 * SysLog 切面实现类扩展 for 登录后台用户
 *
 * @author Uncarbon
 */
@Component
public class SysLogAspectExtensionForSysUserLogin implements SysLogAspectExtension {

    @Override
    public void beforeSaving(SysLog sysLogAnnotation, ProceedingJoinPoint point, SysLogEntity sysLogEntity) {
        if (SysConstant.SysLogOperation.SYS_USER_LOGIN.equals(sysLogAnnotation.value())) {
            // 仅针对特定注解扩展
            for (Object arg : point.getArgs()) {
                if (arg instanceof SysUserLoginDTO) {
                    SysUserLoginDTO dto = (SysUserLoginDTO) arg;
                    sysLogEntity.setUsername(dto.getUsername());
                    sysLogEntity.setIp(dto.getClientIP());
                }
            }
        }
    }
}
