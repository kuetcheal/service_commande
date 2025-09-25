package com.javathinked.example.demo_spring.service;

import com.javathinked.example.demo_spring.model.Order;
import com.javathinked.example.demo_spring.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setCustomerId(1L);
        testOrder.setTotalAmount(new BigDecimal("99.99"));
        testOrder.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void shouldSaveOrder() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        Order result = orderService.saveOrder(testOrder);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getCustomerId());
        verify(orderRepository).save(testOrder);
    }

    @Test
    void shouldGetAllOrders() {
        // Given
        List<Order> expectedOrders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        // When
        List<Order> result = orderService.getAllOrders();

        // Then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(orderRepository).findAll();
    }

    @Test
    void shouldGetOrderById() {
        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // When
        Order result = orderService.getOrderById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getCustomerId());
        verify(orderRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Given
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> {
            orderService.getOrderById(999L);
        });
        verify(orderRepository).findById(999L);
    }

    @Test
    void shouldDeleteOrder() {
        // Given
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        // When
        orderService.deleteOrder(1L);

        // Then
        verify(orderRepository).existsById(1L);
        verify(orderRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullOrder() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            orderService.saveOrder(null);
        });
    }

    @Test
    void shouldHandleEmptyOrderList() {
        // Given
        when(orderRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Order> result = orderService.getAllOrders();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepository).findAll();
    }

    @Test
    void shouldHandleMultipleOrders() {
        // Given
        Order order1 = new Order(1L);
        order1.setId(1L);
        Order order2 = new Order(2L);
        order2.setId(2L);
        List<Order> expectedOrders = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        // When
        List<Order> result = orderService.getAllOrders();

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(orderRepository).findAll();
    }

    @Test
    void shouldHandleOrderWithZeroTotalAmount() {
        // Given
        Order orderWithZeroAmount = new Order(1L);
        orderWithZeroAmount.setId(1L);
        orderWithZeroAmount.setTotalAmount(BigDecimal.ZERO);
        when(orderRepository.save(any(Order.class))).thenReturn(orderWithZeroAmount);

        // When
        Order result = orderService.saveOrder(orderWithZeroAmount);

        // Then
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getTotalAmount());
        verify(orderRepository).save(orderWithZeroAmount);
    }

    @Test
    void shouldHandleOrderWithLargeAmount() {
        // Given
        Order orderWithLargeAmount = new Order(1L);
        orderWithLargeAmount.setId(1L);
        orderWithLargeAmount.setTotalAmount(new BigDecimal("999999.99"));
        when(orderRepository.save(any(Order.class))).thenReturn(orderWithLargeAmount);

        // When
        Order result = orderService.saveOrder(orderWithLargeAmount);

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("999999.99"), result.getTotalAmount());
        verify(orderRepository).save(orderWithLargeAmount);
    }
}
