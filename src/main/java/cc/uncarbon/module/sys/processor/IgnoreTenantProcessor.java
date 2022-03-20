package cc.uncarbon.module.sys.processor;

import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class IgnoreTenantProcessor {

    private final ApplicationContext applicationContext;


    @PostConstruct
    public void postConstruct() throws NoSuchFieldException {

        Field interceptorIgnoreCache = InterceptorIgnoreHelper.class.getDeclaredField("INTERCEPTOR_IGNORE_CACHE");
        interceptorIgnoreCache.setAccessible(true);
        interceptorIgnoreCache.get()



        // beanName beanObj
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(Mapper.class);


        beanMap.values().forEach(
                bean -> {
                    Method[] methods = bean.getClass().getDeclaredMethods();
                    for (Method method : methods) {
                        methods.getClass().getAnnotation()
                    }
                }
        );
    }
}
