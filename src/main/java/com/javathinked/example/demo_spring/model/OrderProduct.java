package com.javathinked.example.demo_spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relation -> orders.id (colonne order_id NOT NULL)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore // évite la récursion JSON si tu renvoies un OrderProduct
    private Order order;

    // identifiant du produit (pas de FK cross-service)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    // quantité commandée
    @Column(nullable = false)
    private Integer quantity = 1;

    // prix unitaire figé au moment de la commande
    @Column(name = "unit_price_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceSnapshot = BigDecimal.ZERO;

    public OrderProduct() {}

    public OrderProduct(Order order, Long productId, Integer quantity, BigDecimal unitPriceSnapshot) {
        this.order = order;
        this.productId = productId;
        this.quantity = (quantity == null ? 1 : quantity);
        this.unitPriceSnapshot = (unitPriceSnapshot == null ? BigDecimal.ZERO : unitPriceSnapshot);
    }

    // === Getters / Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPriceSnapshot() { return unitPriceSnapshot; }
    public void setUnitPriceSnapshot(BigDecimal unitPriceSnapshot) { this.unitPriceSnapshot = unitPriceSnapshot; }
}
