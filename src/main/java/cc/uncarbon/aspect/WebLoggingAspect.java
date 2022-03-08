package cc.uncarbon.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Web 请求及响应日志切面
 *
 * @author Uncarbon
 */
@Order(-1)
@Aspect
@Slf4j
@Component
@ConditionalOnProperty(value = "helio.webLogging.enabledRequestLog")
public class WebLoggingAspect {

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {
        // AOP Pointcut
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @Pointcut("within(cc.uncarbon.framework..*) || within(cc.uncarbon.module..*)")
    public void applicationPackagePointcut() {
        // AOP Pointcut
    }

    /**
     * 日志记录请求体及响应体
     * 切面一定程度上来说会有性能损失，影响 QPS，不建议在生产环境使用本切面类
     *
     * @param point 织入点，由切面引擎自动注入
     * @return 织入点最终执行结果
     * @throws Throwable 被抛出的异常
     */
    @Around("restControllerPointcut() && applicationPackagePointcut()")
    public Object restControllerAround(ProceedingJoinPoint point) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        /*
        请求体记录
         */
        StringBuilder reqLog = new StringBuilder(500);
        List<Object> reqLogParameters = new ArrayList<>();

        reqLog.append("\n\n================  Request Start  ================\n");

        // 打印路由  e.g. POST: /api/v1/doSth
        reqLog.append("{}: {} \n");
        reqLogParameters.add(request.getMethod());
        reqLogParameters.add(request.getRequestURI());

        // 打印入参
        reqLog.append("Parameters: {} \n");
        reqLogParameters.add(Arrays.stream(point.getArgs()).map(Object::toString).collect(Collectors.joining("\n")));

        reqLog.append("================   Request End   ================\n");

        log.debug(reqLog.toString(), reqLogParameters.toArray());

        /*
        响应体记录
         */
        StringBuilder repLog = new StringBuilder(200);
        List<Object> repLogParameters = new ArrayList<>();

        repLog.append("\n\n================  Response Start  ================\n");

        long beginAt = System.nanoTime();
        try {
            Object result = point.proceed();
            repLog.append("result: {} \n");
            repLogParameters.add(result);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(point.getArgs()),
                    point.getSignature().getDeclaringTypeName(), point.getSignature().getName());

            throw e;
        } finally {
            long duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - beginAt);
            repLog.append("{}: {} (elapsed {} ms)\n");
            repLogParameters.add(request.getMethod());
            repLogParameters.add(request.getRequestURI());
            repLogParameters.add(duration);
            repLog.append("================   Response End   ================\n");

            log.debug(repLog.toString(), repLogParameters.toArray());
        }
    }
}
