package com.javathinked.example.demo_spring.controller;

import com.javathinked.example.demo_spring.dto.OrderProductDto;
import com.javathinked.example.demo_spring.model.OrderProduct;
import com.javathinked.example.demo_spring.service.OrderProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/order-products") // <-- pas de /api ici
public class OrderProductController {

    private final OrderProductService orderProductService;

    public OrderProductController(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    // { orderId, productId, quantity, unitPriceSnapshot }
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody OrderProductDto dto) {
        OrderProduct saved = orderProductService.createFromDto(dto);
        // 201 Created SANS body -> pas de sérialisation d'entité JPA (évite les 500)
        return ResponseEntity.created(URI.create("/order-products/" + saved.getId())).build();
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
}
