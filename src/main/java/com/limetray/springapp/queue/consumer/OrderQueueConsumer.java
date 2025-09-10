package com.limetray.springapp.queue.consumer;

import com.limetray.springapp.entity.OrderStatus;
import com.limetray.springapp.service.OrderService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableAsync
@EnableScheduling
public class OrderQueueConsumer {
    
    private final BlockingQueue<Long> orderQueue;
    private final OrderService orderService;
    
    @PostConstruct
    public void init() {
        log.info("Order Queue Consumer initialized. Starting to process orders...");
    }
    
    @Scheduled(fixedDelay = 1000) 
    public void processOrders() {
        try {
          
            Long orderId = orderQueue.poll(100, TimeUnit.MILLISECONDS);
            
            if (orderId != null) {
                processOrder(orderId);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Queue consumer interrupted", e);
        } catch (Exception e) {
            log.error("Error processing order from queue", e);
        }
    }
    
    @Async
    private void processOrder(Long orderId) {
        try {
            log.info("Processing order: {}", orderId);
            
            
            Thread.sleep((long) (Math.random() * 3000 + 2000));
            
          
            orderService.updateOrderStatus(orderId, OrderStatus.PROCESSING);
            
          
            Thread.sleep(2000);
            
           
            orderService.updateOrderStatus(orderId, OrderStatus.PROCESSED);
            
            log.info("Order {} processed successfully", orderId);
            
        } catch (Exception e) {
            log.error("Error processing order {}", orderId, e);
            
        }
    }
}