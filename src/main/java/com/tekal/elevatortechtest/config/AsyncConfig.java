package com.tekal.elevatortechtest.config;

import com.tekal.elevatortechtest.model.Elevator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Set;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public ThreadPoolTaskExecutor elevatorTaskExecutor(@Autowired Set<Elevator> elevators) {
        int elevatorCount = elevators.size();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(elevatorCount);
        executor.setMaxPoolSize(elevatorCount);
        executor.setQueueCapacity(25);
        return executor;
    }
}
