package com.javathinked.example.demo_spring.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderProductTest {

    private OrderProduct orderProduct;

    @BeforeEach
    void setUp() {
        orderProduct = new OrderProduct();
    }

    @Test
    void shouldCreateOrderProductWithDefaultConstructor() {
        // When
        OrderProduct newOrderProduct = new OrderProduct();

        // Then
        assertNotNull(newOrderProduct);
        assertNull(newOrderProduct.getId());
        assertNull(newOrderProduct.getOrder());
        assertNull(newOrderProduct.getProductId());
        assertEquals(1, newOrderProduct.getQuantity());
        assertEquals(BigDecimal.ZERO, newOrderProduct.getUnitPriceSnapshot());
    }

    @Test
    void shouldSetAndGetId() {
        // Given
        Long id = 1L;

        // When
        orderProduct.setId(id);

        // Then
        assertEquals(id, orderProduct.getId());
    }

    @Test
    void shouldSetAndGetOrder() {
        // Given
        Order order = new Order(1L);

        // When
        orderProduct.setOrder(order);

        // Then
        assertEquals(order, orderProduct.getOrder());
    }

    @Test
    void shouldSetAndGetProductId() {
        // Given
        Long productId = 123L;

        // When
        orderProduct.setProductId(productId);

        // Then
        assertEquals(productId, orderProduct.getProductId());
    }

    @Test
    void shouldSetAndGetQuantity() {
        // Given
        Integer quantity = 5;

        // When
        orderProduct.setQuantity(quantity);

        // Then
        assertEquals(quantity, orderProduct.getQuantity());
    }

    @Test
    void shouldSetAndGetUnitPriceSnapshot() {
        // Given
        BigDecimal unitPriceSnapshot = new BigDecimal("29.99");

        // When
        orderProduct.setUnitPriceSnapshot(unitPriceSnapshot);

        // Then
        assertEquals(unitPriceSnapshot, orderProduct.getUnitPriceSnapshot());
    }

    @Test
    void shouldHandleNullValues() {
        // When
        orderProduct.setId(null);
        orderProduct.setOrder(null);
        orderProduct.setProductId(null);
        orderProduct.setQuantity(null);
        orderProduct.setUnitPriceSnapshot(null);

        // Then
        assertNull(orderProduct.getId());
        assertNull(orderProduct.getOrder());
        assertNull(orderProduct.getProductId());
        assertNull(orderProduct.getQuantity());
        assertNull(orderProduct.getUnitPriceSnapshot());
    }

    @Test
    void shouldHandleZeroValues() {
        // Given
        Integer zeroQuantity = 0;
        BigDecimal zeroUnitPriceSnapshot = BigDecimal.ZERO;

        // When
        orderProduct.setQuantity(zeroQuantity);
        orderProduct.setUnitPriceSnapshot(zeroUnitPriceSnapshot);

        // Then
        assertEquals(0, orderProduct.getQuantity());
        assertEquals(BigDecimal.ZERO, orderProduct.getUnitPriceSnapshot());
    }

    @Test
    void shouldHandleLargeValues() {
        // Given
        Long largeId = Long.MAX_VALUE;
        Long largeProductId = Long.MAX_VALUE;
        Integer largeQuantity = Integer.MAX_VALUE;
        BigDecimal largeUnitPriceSnapshot = new BigDecimal("999999.99");

        // When
        orderProduct.setId(largeId);
        orderProduct.setProductId(largeProductId);
        orderProduct.setQuantity(largeQuantity);
        orderProduct.setUnitPriceSnapshot(largeUnitPriceSnapshot);

        // Then
        assertEquals(Long.MAX_VALUE, orderProduct.getId());
        assertEquals(Long.MAX_VALUE, orderProduct.getProductId());
        assertEquals(Integer.MAX_VALUE, orderProduct.getQuantity());
        assertEquals(new BigDecimal("999999.99"), orderProduct.getUnitPriceSnapshot());
    }

    @Test
    void shouldHandleNegativeValues() {
        // Given
        Integer negativeQuantity = -1;
        BigDecimal negativeUnitPriceSnapshot = new BigDecimal("-10.00");

        // When
        orderProduct.setQuantity(negativeQuantity);
        orderProduct.setUnitPriceSnapshot(negativeUnitPriceSnapshot);

        // Then
        assertEquals(-1, orderProduct.getQuantity());
        assertEquals(new BigDecimal("-10.00"), orderProduct.getUnitPriceSnapshot());
    }
}
