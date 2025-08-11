package com.javathinked.example.demo_spring.repository;

import com.javathinked.example.demo_spring.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
