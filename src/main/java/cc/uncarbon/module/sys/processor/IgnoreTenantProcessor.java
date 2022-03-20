package cc.uncarbon.module.sys.processor;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class IgnoreTenantProcessor {

    private final ApplicationContext applicationContext;


    @PostConstruct
    public void postConstruct() {
        Object fieldValue = ReflectUtil.getFieldValue(InterceptorIgnoreHelper.class, "INTERCEPTOR_IGNORE_CACHE");
        ConcurrentHashMap<String, InterceptorIgnoreHelper.InterceptorIgnoreCache> interceptorIgnoreCache = (ConcurrentHashMap<String, InterceptorIgnoreHelper.InterceptorIgnoreCache>) fieldValue;


        /*// beanName beanObj
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(RequiredIgnoredTenant.class);

        beanMap.values().forEach(
                bean -> {
                    log.info("bean name = {}", bean.getClass().getName());
                    Method[] methods = bean.getClass().getDeclaredMethods();
                    for (Method method : methods) {
                        log.info("反射method: {}", method);

                        if (methods.getClass().isAnnotationPresent(IgnoredTenant.class)) {
                            log.info("添加租户忽略：{}", method.getName());

                            interceptorIgnoreCache.put(method.getName(), InterceptorIgnoreHelper.InterceptorIgnoreCache.builder()
                                    .tenantLine(true)
                                    .build());
                        }
                    }
                }
        );*/

        interceptorIgnoreCache.put("cc.uncarbon.module.sys.mapper.SysUserMapper.getUserByPin", InterceptorIgnoreHelper.InterceptorIgnoreCache.builder()
                .tenantLine(true)
                .build())
                ;

        /*interceptorIgnoreCache.put("cc.uncarbon.module.sys.mapper.SysUserMapper.getBaseInfoByUserId", InterceptorIgnoreHelper.InterceptorIgnoreCache.builder()
                .tenantLine(true)
                .build())
        ;*/

        log.info("interceptorIgnoreCache={}", interceptorIgnoreCache);
    }
}
