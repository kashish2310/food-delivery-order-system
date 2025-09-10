package com.limetray.springapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfig {
    
    @Bean
    public BlockingQueue<Long> orderQueue() {
      
        return new LinkedBlockingQueue<>(1000);
    }
}