package cc.uncarbon.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * @author Uncarbon
 */
@Slf4j
@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AsyncConfiguration implements AsyncConfigurer {

    private final TaskExecutionProperties taskExecutionProperties;


    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        final String threadNamePrefix = "taskExecutor-";

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小 or 10
        executor.setCorePoolSize(taskExecutionProperties.getPool().getCoreSize());
        // 最大线程数 or 50
        executor.setMaxPoolSize(taskExecutionProperties.getPool().getMaxSize());
        // 队列容量 or 10
        executor.setQueueCapacity(taskExecutionProperties.getPool().getQueueCapacity());
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
        log.debug("Creating Default Async Task Executor");
        return this.taskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            log.error("执行异步任务'{}'出错", method);
            ex.printStackTrace();
        };
    }
}
