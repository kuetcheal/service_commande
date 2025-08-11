package com.javathinked.example.demo_spring.service;

import com.javathinked.example.demo_spring.model.Order;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    void deleteOrder(Long id);
}
