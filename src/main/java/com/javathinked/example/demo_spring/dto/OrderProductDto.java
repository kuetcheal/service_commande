package com.javathinked.example.demo_spring.dto;

import jakarta.validation.constraints.NotNull;

public class OrderProductDto {

    @NotNull
    private Long orderId;

    @NotNull
    private Long productId;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}
