package com.javathinked.example.demo_spring.mapper;

import com.javathinked.example.demo_spring.dto.OrderDto;
import com.javathinked.example.demo_spring.model.Order;

public class OrderMapper {

    private OrderMapper() {}

    public static Order fromCreateDto(OrderDto dto) {
        Order o = new Order();
        o.setCustomerId(dto.getCustomerId());
        return o;
    }

    public static OrderDto toDto(Order o) {
        OrderDto d = new OrderDto();
        d.setId(o.getId());
        d.setCustomerId(o.getCustomerId());
        d.setCreatedAt(o.getCreatedAt());
        d.setTotalAmount(o.getTotalAmount());
        return d;
    }
}
