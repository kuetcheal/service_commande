package com.javathinked.example.demo_spring.controller;

import com.javathinked.example.demo_spring.dto.OrderProductDto;
import com.javathinked.example.demo_spring.dto.events.OrderCreatedEvent;
import com.javathinked.example.demo_spring.messaging.OrderPublisher;
import com.javathinked.example.demo_spring.model.OrderProduct;
import com.javathinked.example.demo_spring.service.OrderProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/order-products")
public class OrderProductController {

    private final OrderProductService orderProductService;
    private final OrderPublisher orderPublisher;

    public OrderProductController(OrderProductService orderProductService, OrderPublisher orderPublisher) {
        this.orderProductService = orderProductService;
        this.orderPublisher = orderPublisher;
    }

    // POST via DTO { "orderId": 1, "productId": 2 }
    @PostMapping
    public ResponseEntity<OrderProduct> create(@Valid @RequestBody OrderProductDto dto) {
        OrderProduct saved = orderProductService.createFromDto(dto);
        return ResponseEntity
                .created(URI.create("/order-products/" + saved.getId()))
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<OrderProduct>> getAll() {
        return ResponseEntity.ok(orderProductService.getAllOrderProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderProduct> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderProductService.getOrderProductById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderProductService.deleteOrderProduct(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoint de test RabbitMQ ---
    // POST http://localhost:8082/order-products/test-publish?orderId=1&customerId=12&productIds=2,5
    @PostMapping("/test-publish")
    public ResponseEntity<String> testPublishOrderFromOrderProduct(
            @RequestParam Long orderId,
            @RequestParam Long customerId,
            @RequestParam List<Long> productIds
    ) {
        OrderCreatedEvent evt = new OrderCreatedEvent(
                orderId,
                customerId,
                productIds,
                Instant.now(),
                "service-commande",
                1
        );
        orderPublisher.publishOrderCreated(evt);
        return ResponseEntity.ok("OrderCreatedEvent (via OrderProductController) publié vers RabbitMQ");
    }
}
