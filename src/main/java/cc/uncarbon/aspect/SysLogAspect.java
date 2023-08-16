package cc.uncarbon.aspect;

import cc.uncarbon.framework.core.context.UserContextHolder;
import cc.uncarbon.framework.web.util.IPUtil;
import cc.uncarbon.module.sys.annotation.SysLog;
import cc.uncarbon.module.sys.constant.SysConstant;
import cc.uncarbon.module.sys.enums.SysLogStatusEnum;
import cc.uncarbon.module.sys.extension.SysLogAspectExtension;
import cc.uncarbon.module.sys.extension.impl.DefaultSysLogAspectExtension;
import cc.uncarbon.module.sys.model.request.AdminInsertSysLogDTO;
import cc.uncarbon.module.sys.model.response.IPLocationBO;
import cc.uncarbon.module.sys.service.SysLogService;
import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SysLog 切面实现类
 *
 * @author Uncarbon
 * @author ruoyi
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SysLogAspect {

    /**
     * 用于数据脱敏的Bean复制选项
     */
    private static final CopyOptions MASKING_COPY_OPTIONS = new CopyOptions()
            .setIgnoreNullValue(false)
            .setIgnoreProperties(SysConstant.SENSITIVE_FIELDS);

    /**
     * 用于数据脱敏后JSON字符串的序列化配置
     */
    private static final JSONConfig TO_JSON_STR_JSON_CONFIG = new JSONConfig()
            .setNatureKeyComparator()
            .setDateFormat(DatePattern.NORM_DATETIME_MS_PATTERN);

    /**
     * 最大数据文本保存长度
     */
    private static final int MAX_STRING_SAVE_LENGTH = 3000;

    private static final SysLogAspectExtension DEFAULT_SYS_LOG_ASPECT_EXTENSION = new DefaultSysLogAspectExtension();

    private final SysLogService sysLogService;
    private final ThreadPoolTaskExecutor taskExecutor;


    /**
     * 顺利运行
     */
    @AfterReturning(pointcut = "@annotation(annotation)", returning = "ret")
    public void returning(JoinPoint joinPoint, SysLog annotation, Object ret) {
        if (!ArrayUtil.contains(annotation.when(), SysLog.When.SUCCESS)) {
            // 系统日志保存时机不包含“成功时”
            return;
        }

        HttpServletRequest request = SpringMVCUtil.getRequest();

        if (!annotation.syncSave()) {
            // 异步保存
            saveSysLogAsync(joinPoint, annotation, request, null, ret);
            return;
        }

        saveSysLog(joinPoint, annotation, request, null, ret);
    }

    /**
     * 出现异常
     */
    @AfterThrowing(value = "@annotation(annotation)", throwing = "e")
    public void throwing(JoinPoint joinPoint, SysLog annotation, Throwable e) {
        if (!ArrayUtil.contains(annotation.when(), SysLog.When.FAILED)) {
            // 系统日志保存时机不包含“失败时”
            return;
        }

        HttpServletRequest request = SpringMVCUtil.getRequest();

        if (!annotation.syncSave()) {
            // 异步保存
            saveSysLogAsync(joinPoint, annotation, request, e, null);
            return;
        }

        saveSysLog(joinPoint, annotation, request, e, null);
    }

    /**
     * 同步保存系统日志
     * @param joinPoint 切点
     * @param annotation 注解实例
     * @param request 当前线程HTTP请求
     * @param e 异常实例，可以为null
     * @param ret 返回值，可以为null
     */
    private void saveSysLog(final JoinPoint joinPoint, SysLog annotation, final HttpServletRequest request, final Throwable e, Object ret) {
        // SysLog 切面实现类扩展
        SysLogAspectExtension extensionInstance = DEFAULT_SYS_LOG_ASPECT_EXTENSION;
        Class<? extends SysLogAspectExtension> extensionClazz = annotation.extension();
        if (extensionClazz != null && extensionClazz != DefaultSysLogAspectExtension.class) {
            // 有自定义扩展点
            extensionInstance = ReflectUtil.newInstance(extensionClazz);
        }

        AdminInsertSysLogDTO dto = new AdminInsertSysLogDTO()
                // 记录操作人
                .setUserId(UserContextHolder.getUserId())
                .setUsername(UserContextHolder.getUserName())
                // 记录请求方法
                .setMethod(StrUtil.builder(
                        joinPoint.getTarget().getClass().getName(),
                        "#",
                        joinPoint.getSignature().getName()
                ).toString())
                // 记录操作内容
                .setOperation(annotation.value())
                // 默认置为成功
                .setStatus(SysLogStatusEnum.SUCCESS);

        /*
        记录请求参数
         */
        Map<Object, Object> afterMasked = new LinkedHashMap<>(32, 1);
        String params = Arrays.stream(joinPoint.getArgs()).map(
                item -> {
                    if (ClassUtil.isBasicType(item.getClass())) {
                        // 基元类型 OR 其包装类型，保存在DB时保持原样
                        return StrUtil.toStringOrNull(item);
                    }

                    // 先去除敏感字段后再入库
                    afterMasked.clear();
                    BeanUtil.copyProperties(item, afterMasked, MASKING_COPY_OPTIONS);
                    return JSONUtil.toJsonStr(afterMasked, TO_JSON_STR_JSON_CONFIG);
                }
        ).collect(Collectors.joining(StrPool.LF));
        if (StrUtil.length(params) > MAX_STRING_SAVE_LENGTH) {
            params = StrUtil.subPre(params, MAX_STRING_SAVE_LENGTH);
        }
        dto.setParams(params);

        /*
        记录IP地址
        https://gitee.com/uncarbon97/helio-boot/issues/I5KN1X
         */
        String ip = UserContextHolder.getClientIP();
        if (StrUtil.isEmpty(ip)) {
            // 兜底处理，直接从当前线程的请求头中拿
            ip = IPUtil.getClientIPAddress(request, 0);
        }
        dto.setIp(ip);

        if (e != null) {
            // 异常不为空，置状态为失败
            dto.setStatus(SysLogStatusEnum.FAILED);

            // 错误原因堆栈
            dto.setErrorStacktrace(ExceptionUtil.stacktraceToString(e, MAX_STRING_SAVE_LENGTH));
        }

        // UA；Spring框架已经做了简单的防注入过滤
        dto.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));

        if (annotation.queryIPLocation()) {
            // 查询IP属地
            IPLocationBO ipLocation = extensionInstance.queryIPLocation(ip);
            dto
                    .setIpLocationRegionName(ipLocation.getRegionName())
                    .setIpLocationProvinceName(ipLocation.getProvinceName())
                    .setIpLocationCityName(ipLocation.getCityName())
                    .setIpLocationDistrictName(ipLocation.getDistrictName());
        }

        // 扩展：保存到 DB 前
        extensionInstance.beforeSaving(dto, joinPoint, annotation, e, ret);

        // 保存系统日志
        sysLogService.adminInsert(dto);
    }

    /**
     * 异步保存系统日志
     */
    public void saveSysLogAsync(final JoinPoint joinPoint, SysLog annotation, final HttpServletRequest request, final Throwable e, Object ret) {
        taskExecutor.submit(
                () -> this.saveSysLog(joinPoint, annotation, request, e, ret)
        );
    }
}
