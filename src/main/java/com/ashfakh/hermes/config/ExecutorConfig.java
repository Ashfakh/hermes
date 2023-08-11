package com.ashfakh.hermes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

@Configuration
public class ExecutorConfig {


    @Bean
    public ExtendedExecutor executor() {
        return new ExtendedExecutor(10, Integer.MAX_VALUE, 60L,
                TimeUnit.SECONDS, new SynchronousQueue<>());
    }
}
