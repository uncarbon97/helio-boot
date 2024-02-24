package cc.uncarbon.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author Uncarbon
 */
@Slf4j
@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfiguration implements AsyncConfigurer {

    private final TaskExecutionProperties taskExecutionProperties;


    /**
     * 创建默认线程池
     * 加 @Primary 注解以确保依赖注入时，获取到的是这个Bean
     */
    @Primary
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        final String threadNamePrefix = "taskExecutor-";

        if (log.isDebugEnabled()) {
            log.debug("[异步任务线程池] 创建默认线程池【taskExecutor】，该线程池参数可通过 spring.task.execution 调节 >> "
                            + "corePoolSize核心线程池大小={}, maxPoolSize最大线程数={}, queueCapacity队列容量={}"
                            + ", rejectedExecutionHandler拒绝策略={}",
                    taskExecutionProperties.getPool().getCoreSize(),
                    taskExecutionProperties.getPool().getMaxSize(),
                    taskExecutionProperties.getPool().getQueueCapacity(),
                    "CallerRunsPolicy"
            );
        }

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小，默认 8
        executor.setCorePoolSize(taskExecutionProperties.getPool().getCoreSize());
        // 最大线程数，默认 Integer.MAX_VALUE
        executor.setMaxPoolSize(taskExecutionProperties.getPool().getMaxSize());
        // 队列容量，默认 Integer.MAX_VALUE
        executor.setQueueCapacity(taskExecutionProperties.getPool().getQueueCapacity());
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程名前缀
        executor.setThreadNamePrefix(threadNamePrefix);
        // 这个配置是为了graceful shutdown?
        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return this.taskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.error("[异步任务线程池] 执行异步任务【{}】时出错 >> 堆栈\t\n", method, ex);
    }
}
