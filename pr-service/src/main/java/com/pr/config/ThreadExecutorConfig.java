package com.pr.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


@Configuration
public class ThreadExecutorConfig {

    private static final Logger logger =
            LoggerFactory.getLogger(ThreadExecutorConfig.class);

    @Value("${app.executor.core-pool-size}")
    private int corePoolSize;

    @Value("${app.executor.max-pool-size}")
    private int maxPoolSize;

    @Value("${app.executor.queue-capacity}")
    private int queueCapacity;

    @Value("${app.executor.thread-name-prefix}")
    private String threadNamePrefix;

    @Bean(name = "sharedTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();

        logger.info("ThreadPoolTaskExecutor initialized. corePoolSize={}, maxPoolSize={}, queueCapacity={}", corePoolSize, maxPoolSize, queueCapacity);

        return executor;
    }
}
