package me.brilliant.system.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Star Chou
 * @description /
 * @create 2024/7/17
 */
@Data
@Component
public class AsyncTaskProperties {

    public static int corePoolSize;

    public static int maxPoolSize;

    public static int keepAliveSeconds;

    public static int queueCapacity;

    @Value("${task.pool.core-pool-size}")
    public void setCorePoolSize(int corePoolSize) {
        AsyncTaskProperties.corePoolSize = corePoolSize;
    }

    @Value("${task.pool.max-pool-size}")
    public void setMaxPoolSize(int maxPoolSize) {
        AsyncTaskProperties.maxPoolSize = maxPoolSize;
    }

    @Value("${task.pool.keep-alive-seconds}")
    public void setKeepAliveSeconds(int keepAliveSeconds) {
        AsyncTaskProperties.keepAliveSeconds = keepAliveSeconds;
    }

    @Value("${task.pool.queue-capacity}")
    public void setQueueCapacity(int queueCapacity) {
        AsyncTaskProperties.queueCapacity = queueCapacity;
    }
}
