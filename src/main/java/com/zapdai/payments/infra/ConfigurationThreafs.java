package com.zapdai.payments.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
@Configuration
public class ConfigurationThreafs {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 10 threads sempre prontas
        executor.setMaxPoolSize(50);  // até 50 threads simultâneas
        executor.setQueueCapacity(500); // 500 requisições em espera
        executor.setThreadNamePrefix("Zapdai-Async-");
        executor.initialize();
        return executor;
    }
}
