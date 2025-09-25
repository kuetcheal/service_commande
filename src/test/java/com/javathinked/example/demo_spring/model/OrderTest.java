package com.javathinked.example.demo_spring.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void shouldCreateOrderWithDefaultConstructor() {
        // When
        Order newOrder = new Order();

        // Then
        assertNotNull(newOrder);
        assertNull(newOrder.getId());
        assertNull(newOrder.getCustomerId());
        assertNull(newOrder.getCreatedAt());
        assertEquals(BigDecimal.ZERO, newOrder.getTotalAmount());
        assertNotNull(newOrder.getOrderProducts());
        assertTrue(newOrder.getOrderProducts().isEmpty());
    }

    @Test
    void shouldCreateOrderWithCustomerId() {
        // Given
        Long customerId = 1L;

        // When
        Order newOrder = new Order(customerId);

        // Then
        assertNotNull(newOrder);
        assertEquals(customerId, newOrder.getCustomerId());
        assertEquals(BigDecimal.ZERO, newOrder.getTotalAmount());
        assertNotNull(newOrder.getOrderProducts());
        assertTrue(newOrder.getOrderProducts().isEmpty());
    }

    @Test
    void shouldSetAndGetId() {
        // Given
        Long id = 1L;

        // When
        order.setId(id);

        // Then
        assertEquals(id, order.getId());
    }

    @Test
    void shouldSetAndGetCustomerId() {
        // Given
        Long customerId = 123L;

        // When
        order.setCustomerId(customerId);

        // Then
        assertEquals(customerId, order.getCustomerId());
    }

    @Test
    void shouldSetAndGetCreatedAt() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        // When
        order.setCreatedAt(now);

        // Then
        assertEquals(now, order.getCreatedAt());
    }

    @Test
    void shouldSetAndGetTotalAmount() {
        // Given
        BigDecimal amount = new BigDecimal("99.99");

        // When
        order.setTotalAmount(amount);

        // Then
        assertEquals(amount, order.getTotalAmount());
    }

    @Test
    void shouldSetAndGetOrderProducts() {
        // Given
        List<OrderProduct> orderProducts = new ArrayList<>();
        OrderProduct orderProduct = new OrderProduct();
        orderProducts.add(orderProduct);

        // When
        order.setOrderProducts(orderProducts);

        // Then
        assertEquals(orderProducts, order.getOrderProducts());
        assertEquals(1, order.getOrderProducts().size());
    }

    @Test
    void shouldHandleNullValues() {
        // When
        order.setId(null);
        order.setCustomerId(null);
        order.setCreatedAt(null);
        order.setTotalAmount(null);
        order.setOrderProducts(null);

        // Then
        assertNull(order.getId());
        assertNull(order.getCustomerId());
        assertNull(order.getCreatedAt());
        assertNull(order.getTotalAmount());
        assertNull(order.getOrderProducts());
    }

    @Test
    void shouldHandleZeroValues() {
        // Given
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // When
        order.setTotalAmount(zeroAmount);

        // Then
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
    }

    @Test
    void shouldHandleLargeValues() {
        // Given
        Long largeId = Long.MAX_VALUE;
        Long largeCustomerId = Long.MAX_VALUE;
        BigDecimal largeAmount = new BigDecimal("999999.99");

        // When
        order.setId(largeId);
        order.setCustomerId(largeCustomerId);
        order.setTotalAmount(largeAmount);

        // Then
        assertEquals(Long.MAX_VALUE, order.getId());
        assertEquals(Long.MAX_VALUE, order.getCustomerId());
        assertEquals(new BigDecimal("999999.99"), order.getTotalAmount());
    }

    @Test
    void shouldHandleEmptyOrderProductsList() {
        // Given
        List<OrderProduct> emptyList = new ArrayList<>();

        // When
        order.setOrderProducts(emptyList);

        // Then
        assertNotNull(order.getOrderProducts());
        assertTrue(order.getOrderProducts().isEmpty());
    }
}
