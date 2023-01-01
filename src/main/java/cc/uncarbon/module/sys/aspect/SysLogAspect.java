package cc.uncarbon.module.sys.aspect;

import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.satoken.util.IPUtil;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.aspect.extension.SysLogAspectExtension;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
import cc.uncarbon.module.sys.model.request.AdminInsertSysLogDTO;
import cc.uncarbon.module.sys.service.SysLogService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.text.StrPool;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * SysLog 切面实现类
 *
 * @author Uncarbon
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SysLogAspect {

    private final SysLogService sysLogService;
    private final ObjectProvider<SysLogAspectExtension> extensions;
    // Bean 属性复制配置项
    private static final CopyOptions copyOptions4MaskingArgs = new CopyOptions();


    static {
        copyOptions4MaskingArgs.setIgnoreNullValue(false);
        copyOptions4MaskingArgs.setIgnoreProperties(SysConstant.SENSITIVE_FIELDS);
    }

    @Pointcut("@annotation(cc.uncarbon.module.sys.annotation.SysLog)")
    public void sysLogPointcut() {
        // AOP Pointcut
    }

    @Around("sysLogPointcut()")
    public Object sysLogAround(ProceedingJoinPoint point) throws Throwable {
        // 切面点执行结果
        Object executeResult = null;
        // 切面点执行是否成功
        boolean executeSuccessFlag = false;
        // 切面点执行过程中抛出的异常
        Exception executeFailedException = null;
        try {
            executeResult = point.proceed();
            executeSuccessFlag = true;
        } catch (Exception e) {
            // store it
            executeFailedException = e;
        }

        /*
        记录对应系统日志
         */
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        SysLog sysLogAnnotation = methodSignature.getMethod().getAnnotation(SysLog.class);

        if (sysLogAnnotation.syncSaving()) {
            this.sysLogSaving(point, methodSignature, sysLogAnnotation, executeSuccessFlag);
        } else {
            this.sysLogSavingAsync(point, methodSignature, sysLogAnnotation, executeSuccessFlag);
        }

        /*
        如果执行过程中存在异常则抛出
         */
        if (executeFailedException != null) {
            throw executeFailedException;
        }

        return executeResult;
    }

    /**
     * 同步保存系统日志
     */
    private void sysLogSaving(ProceedingJoinPoint point,
                              MethodSignature methodSignature,
                              SysLog sysLogAnnotation,
                              boolean executeSuccessFlag
    ) {
        AdminInsertSysLogDTO dto = new AdminInsertSysLogDTO()
                // 记录操作人
                .setUserId(UserContextHolder.getUserId())
                .setUsername(UserContextHolder.getUserName())
                // 记录请求方法
                .setMethod(methodSignature.toString())
                // 记录操作内容
                .setOperation(sysLogAnnotation.value());

        /*
        记录请求参数
         */
        HashMap<Object, Object> afterMasked = new HashMap<>();
        dto.setParams(Arrays.stream(point.getArgs()).map(
                each -> {
                    // 先去除敏感字段后再入库
                    afterMasked.clear();
                    BeanUtil.copyProperties(each, afterMasked, copyOptions4MaskingArgs);
                    return JSONUtil.toJsonStr(afterMasked);
                }
        ).collect(Collectors.joining(StrPool.COMMA)));

        /*
        记录IP地址
        https://gitee.com/uncarbon97/helio-boot/issues/I5KN1X
         */
        String ip = UserContextHolder.getClientIP();
        if (ip == null) {
            // 兜底处理，直接从当前线程的请求头中拿
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                ip = IPUtil.getClientIPAddress(requestAttributes.getRequest());
            }
        }
        dto.setIp(ip);

        // 记录状态
        dto.setStatus(executeSuccessFlag ? SysLogStatusEnum.SUCCESS : SysLogStatusEnum.FAILED);

        // 执行扩展 - 保存到 DB 前
        for (SysLogAspectExtension extension : extensions) {
            extension.beforeSaving(sysLogAnnotation, point, dto);
        }

        // 保存系统日志
        sysLogService.adminInsert(dto);
    }

    /**
     * 异步保存系统日志
     */
    @Async(value = "taskExecutor")
    public void sysLogSavingAsync(ProceedingJoinPoint point,
                                  MethodSignature methodSignature,
                                  SysLog sysLogAnnotation,
                                  boolean executeSuccessFlag
    ) {
        this.sysLogSaving(point, methodSignature, sysLogAnnotation, executeSuccessFlag);
    }

}
