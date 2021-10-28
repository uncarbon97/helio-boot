package cc.uncarbon.module.sys.aspect;

import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.entity.SysLogEntity;
import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
import cc.uncarbon.module.sys.service.SysLogService;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * SysLog切面实现类
 * @author Uncarbon
 */
@Aspect
@Component
@Slf4j
@Data
public class SysLogAspect {

    @Resource
    private SysLogService sysLogService;


    @Pointcut("@annotation(cc.uncarbon.module.sys.annotation.SysLog)")
    public void sysLogPointcut() {
        // AOP Pointcut
    }

    @Around("sysLogPointcut()")
    public Object sysLogAround(ProceedingJoinPoint point) throws Throwable {
        // --------------------Begin @SysLog--------------------

        Object executeResult;
        executeResult = point.proceed();

        SysLogEntity sysLogEntity = new SysLogEntity()
                .setUserId(UserContextHolder.getUserId())
                .setUsername(UserContextHolder.getUserName())
                ;

        // 请求方法
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        sysLogEntity.setMethod(methodSignature.toString());

        // 操作内容
        SysLog sysLogAnnotation = methodSignature.getMethod().getAnnotation(SysLog.class);
        sysLogEntity.setOperation(sysLogAnnotation.value());

        // 请求参数
        sysLogEntity.setParams(Arrays.stream(point.getArgs()).map(JSONUtil::toJsonStr).collect(Collectors.joining(",")));

        // IP地址
        sysLogEntity.setIp(UserContextHolder.getUserContext().getClientIP());

        // 状态
        sysLogEntity.setStatus(SysLogStatusEnum.SUCCESS);

        this.callSysLogServiceSave(sysLogEntity);
        // --------------------End @SysLog--------------------

        return executeResult;
    }

    @Async(value = "taskExecutor")
    void callSysLogServiceSave(SysLogEntity sysLogEntity) {
        sysLogService.save(sysLogEntity);
    }

}
