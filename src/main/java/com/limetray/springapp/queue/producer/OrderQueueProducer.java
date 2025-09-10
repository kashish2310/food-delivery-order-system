package com.limetray.springapp.queue.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.BlockingQueue;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderQueueProducer {
    
    private final BlockingQueue<Long> orderQueue;
    
    public void sendOrder(Long orderId) {
        try {
            boolean added = orderQueue.offer(orderId);
            if (added) {
                log.info("Order {} added to queue for processing", orderId);
            } else {
                log.error("Failed to add order {} to queue - queue might be full", orderId);
            }
        } catch (Exception e) {
            log.error("Error sending order {} to queue", orderId, e);
        }
    }
}