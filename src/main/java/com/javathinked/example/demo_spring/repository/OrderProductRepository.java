package com.javathinked.example.demo_spring.repository;

import com.javathinked.example.demo_spring.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
