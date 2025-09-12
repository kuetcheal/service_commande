package com.javathinked.example.demo_spring.mapper;

import com.javathinked.example.demo_spring.dto.OrderProductDto;
import com.javathinked.example.demo_spring.model.Order;
import com.javathinked.example.demo_spring.model.OrderProduct;

public class OrderProductMapper {

    private OrderProductMapper() {}

    /** Crée une entité à partir d'un DTO + l'Order déjà chargé. */
    public static OrderProduct fromDto(Order order, OrderProductDto dto) {
        OrderProduct op = new OrderProduct();
        op.setOrder(order);
        op.setProductId(dto.getProductId());
        op.setQuantity(dto.getQuantity() == null ? 1 : dto.getQuantity());
        op.setUnitPriceSnapshot(dto.getUnitPriceSnapshot());
        return op;
    }

    /** Transforme une entité en DTO. */
    public static OrderProductDto toDto(OrderProduct op) {
        OrderProductDto dto = new OrderProductDto();
        dto.setOrderId(op.getOrder().getId());
        dto.setProductId(op.getProductId());
        dto.setQuantity(op.getQuantity());
        dto.setUnitPriceSnapshot(op.getUnitPriceSnapshot());
        return dto;
    }
}
