package com.javathinked.example.demo_spring.service;

import com.javathinked.example.demo_spring.dto.OrderProductDto;
import com.javathinked.example.demo_spring.model.OrderProduct;

import java.util.List;

public interface OrderProductService {
    // Nouveau: création via DTO
    OrderProduct createFromDto(OrderProductDto dto);

    // CRUD classiques
    OrderProduct saveOrderProduct(OrderProduct orderProduct);
    List<OrderProduct> getAllOrderProducts();
    OrderProduct getOrderProductById(Long id);
    void deleteOrderProduct(Long id);
}
