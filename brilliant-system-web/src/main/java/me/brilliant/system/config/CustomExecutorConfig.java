package me.brilliant.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Star Chou
 * @description 自定义线程池
 * @create 2024/7/17
 */
@Configuration
public class CustomExecutorConfig {

    /**
     * 自定义线程池，用法 @Async
     * @return Executor
     */
    @Bean
    @Primary
    public Executor brilliantAsync() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(AsyncTaskProperties.corePoolSize);
        executor.setMaxPoolSize(AsyncTaskProperties.maxPoolSize);
        executor.setQueueCapacity(AsyncTaskProperties.queueCapacity);
        executor.setThreadNamePrefix("brilliant-async-");
        executor.setKeepAliveSeconds(AsyncTaskProperties.keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 自定义线程池
     * @return Executor
     */
    @Bean("taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15);
        executor.setQueueCapacity(50);
        executor.setKeepAliveSeconds(AsyncTaskProperties.keepAliveSeconds);
        executor.setThreadNamePrefix("brilliant-task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
