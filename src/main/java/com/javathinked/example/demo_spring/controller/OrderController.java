package com.javathinked.example.demo_spring.controller;

import com.javathinked.example.demo_spring.dto.OrderDto;
import com.javathinked.example.demo_spring.dto.events.OrderCreatedEvent;
import com.javathinked.example.demo_spring.mapper.OrderMapper;
import com.javathinked.example.demo_spring.messaging.OrderPublisher;
import com.javathinked.example.demo_spring.model.Order;
import com.javathinked.example.demo_spring.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderPublisher orderPublisher;

    public OrderController(OrderService orderService, OrderPublisher orderPublisher) {
        this.orderService = orderService;
        this.orderPublisher = orderPublisher;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto dto) {
        if (dto.getCustomerId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customerId is required");
        }

        // 1) persister
        Order toSave = OrderMapper.fromCreateDto(dto);
        Order saved  = orderService.saveOrder(toSave);

        // 2) (optionnel) publier un évènement après avoir ajouté les lignes
        // OrderCreatedEvent evt = new OrderCreatedEvent(
        //         saved.getId(), saved.getCustomerId(), List.of(), Instant.now(), "service-commande", 1
        // );
        // orderPublisher.publishOrderCreated(evt);

        OrderDto body = OrderMapper.toDto(saved);
        return ResponseEntity
                .created(URI.create("/api/orders/" + saved.getId()))
                .body(body);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> result = orderService.getAllOrders()
                .stream()
                .map(OrderMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(OrderMapper.toDto(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoint de test RabbitMQ (optionnel) ---
    @PostMapping("/test-publish")
    public ResponseEntity<String> testPublishOrder(
            @RequestParam Long orderId,
            @RequestParam Long customerId,
            @RequestParam List<Long> productIds
    ) {
        OrderCreatedEvent evt = new OrderCreatedEvent(
                orderId, customerId, productIds, Instant.now(), "service-commande", 1
        );
        orderPublisher.publishOrderCreated(evt);
        return ResponseEntity.ok("OrderCreatedEvent publié vers RabbitMQ");
    }
}
