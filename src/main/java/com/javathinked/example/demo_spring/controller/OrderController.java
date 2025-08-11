package com.javathinked.example.demo_spring.controller;

import com.javathinked.example.demo_spring.dto.events.OrderCreatedEvent;
import com.javathinked.example.demo_spring.messaging.OrderPublisher;
import com.javathinked.example.demo_spring.model.Order;
import com.javathinked.example.demo_spring.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // 1) Persist
        Order saved = orderService.saveOrder(order);

        // 2) (Optionnel) Publier l’événement réel ici si tu as les productIds
        // List<Long> productIds = orderService.findProductIdsForOrder(saved.getId()); // si tu as cette méthode
        // OrderCreatedEvent evt = new OrderCreatedEvent(saved.getId(), saved.getCustomerId(), productIds, Instant.now(), "service-commande", 1);
        // orderPublisher.publishOrderCreated(evt);

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoint de test RabbitMQ ---
    // POST http://localhost:8082/api/orders/test-publish?orderId=1&customerId=12&productIds=2,5
    @PostMapping("/test-publish")
    public ResponseEntity<String> testPublishOrder(
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
        return ResponseEntity.ok("OrderCreatedEvent publié vers RabbitMQ");
    }
}
