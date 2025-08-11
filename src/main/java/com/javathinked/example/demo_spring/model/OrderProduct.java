package com.javathinked.example.demo_spring.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation vers Order (corrige le "mappedBy = order" dans Order.java)
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // On stocke uniquement l'identifiant du produit (clé étrangère logique)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    // === Constructeurs ===
    public OrderProduct() {}

    public OrderProduct(Order order, Long productId) {
        this.order = order;
        this.productId = productId;
    }

    // === Getters / Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
}
