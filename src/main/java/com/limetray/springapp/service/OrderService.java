package com.limetray.springapp.service;

import com.limetray.springapp.dto.request.OrderRequest;
import com.limetray.springapp.dto.response.OrderResponse;
import com.limetray.springapp.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    Page<OrderResponse> getAllOrders(Pageable pageable);
    OrderResponse getOrderById(Long id);
    OrderResponse updateOrderStatus(Long id, OrderStatus status);
    OrderStatus getOrderStatus(Long id);
}