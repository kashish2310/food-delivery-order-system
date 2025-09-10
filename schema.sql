```sql
-- Food Delivery Order Management System
-- Database Schema Migration Script
-- Version: 1.0


-- Create database if not exists
CREATE DATABASE IF NOT EXISTS food_delivery_db;
USE food_delivery_db;

-- Drop existing tables if needed (for fresh setup)
-- Uncomment these lines if you want to reset the database
-- DROP TABLE IF EXISTS orders;

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    items TEXT COMMENT 'JSON string containing order items',
    total_amount DECIMAL(10,2) NOT NULL,
    order_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Indexes for better query performance
    INDEX idx_customer_name (customer_name),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_order_time (order_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- Sample data for testing
-- Uncomment to insert test data
/*
INSERT INTO orders (customer_name, items, total_amount, order_time, status) VALUES
('Kashish Tibrewal', '[{"name":"Pizza","quantity":1,"price":12.99}]', 12.99, NOW(), 'PENDING'),
('Ekta Tibrewal', '[{"name":"Burger","quantity":2,"price":8.99}]', 17.98, NOW(), 'PROCESSED'),
('Aryan Panwar', '[{"name":"Salad","quantity":1,"price":7.99}]', 7.99, NOW(), 'DELIVERED');
*/

