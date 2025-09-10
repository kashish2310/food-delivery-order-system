package com.limetray.springapp.repository;

import com.limetray.springapp.entity.Order;
import com.limetray.springapp.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    
    List<Order> findByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime dateTime);
    
    @Query("SELECT o FROM Order o WHERE o.customerName LIKE %?1%")
    Page<Order> findByCustomerNameContaining(String customerName, Pageable pageable);
}