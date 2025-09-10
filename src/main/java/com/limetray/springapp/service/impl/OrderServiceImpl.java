package com.limetray.springapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.limetray.springapp.dto.request.OrderRequest;
import com.limetray.springapp.dto.response.OrderResponse;
import com.limetray.springapp.entity.Order;
import com.limetray.springapp.entity.OrderStatus;
import com.limetray.springapp.exception.OrderNotFoundException;
import com.limetray.springapp.queue.producer.OrderQueueProducer;
import com.limetray.springapp.repository.OrderRepository;
import com.limetray.springapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final OrderQueueProducer orderQueueProducer;
    
    @Override
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating new order for customer: {}", request.getCustomerName());
        
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setTotalAmount(request.getTotalAmount());
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        
        try {
            
            String itemsJson = objectMapper.writeValueAsString(request.getItems());
            order.setItems(itemsJson);
        } catch (JsonProcessingException e) {
            log.error("Error converting items to JSON", e);
            throw new RuntimeException("Error processing order items");
        }
        
        Order savedOrder = orderRepository.save(order);
        
      
        orderQueueProducer.sendOrder(savedOrder.getId());
        
        return mapToResponse(savedOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        log.info("Fetching orders - page: {}, size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        return orderRepository.findAll(pageable)
                .map(this::mapToResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }
    
    @Override
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        log.info("Updating order {} status to {}", id, status);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        return mapToResponse(updatedOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderStatus getOrderStatus(Long id) {
        return orderRepository.findById(id)
                .map(Order::getStatus)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }
    
    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .items(order.getItems())
                .totalAmount(order.getTotalAmount())
                .orderTime(order.getOrderTime())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}