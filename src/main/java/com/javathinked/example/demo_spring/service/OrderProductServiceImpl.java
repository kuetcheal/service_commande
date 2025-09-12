package com.javathinked.example.demo_spring.service;

import com.javathinked.example.demo_spring.dto.OrderProductDto;
import com.javathinked.example.demo_spring.model.Order;
import com.javathinked.example.demo_spring.model.OrderProduct;
import com.javathinked.example.demo_spring.repository.OrderProductRepository;
import com.javathinked.example.demo_spring.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderProductServiceImpl implements OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;

    public OrderProductServiceImpl(OrderProductRepository orderProductRepository,
                                   OrderRepository orderRepository) {
        this.orderProductRepository = orderProductRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderProduct createFromDto(OrderProductDto dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body is required");
        }
        Order order = orderRepository.findById(dto.getOrderId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found: " + dto.getOrderId()));

        Integer qty = (dto.getQuantity() == null || dto.getQuantity() <= 0) ? 1 : dto.getQuantity();
        BigDecimal unit = dto.getUnitPriceSnapshot() == null ? BigDecimal.ZERO : dto.getUnitPriceSnapshot();

        OrderProduct entity = new OrderProduct();
        entity.setOrder(order);
        entity.setProductId(dto.getProductId());
        entity.setQuantity(qty);
        entity.setUnitPriceSnapshot(unit);

        OrderProduct saved = orderProductRepository.save(entity);

        // Recalcul du total pour CETTE commande
        BigDecimal total = orderProductRepository.sumTotalByOrderId(order.getId());
        order.setTotalAmount(total == null ? BigDecimal.ZERO : total);
        orderRepository.save(order);

        return saved;
    }

    @Override
    public OrderProduct saveOrderProduct(OrderProduct orderProduct) {
        if (orderProduct == null || orderProduct.getOrder() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order is required");
        }
        OrderProduct saved = orderProductRepository.save(orderProduct);

        BigDecimal total = orderProductRepository.sumTotalByOrderId(orderProduct.getOrder().getId());
        Order order = orderProduct.getOrder();
        order.setTotalAmount(total == null ? BigDecimal.ZERO : total);
        orderRepository.save(order);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderProduct> getAllOrderProducts() {
        return orderProductRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderProduct getOrderProductById(Long id) {
        return orderProductRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "OrderProduct not found: " + id));
    }

    @Override
    public void deleteOrderProduct(Long id) {
        OrderProduct op = orderProductRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "OrderProduct not found: " + id));

        Long orderId = op.getOrder().getId();
        orderProductRepository.delete(op);

        BigDecimal total = orderProductRepository.sumTotalByOrderId(orderId);
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Order not found after delete: " + orderId));
        order.setTotalAmount(total == null ? BigDecimal.ZERO : total);
        orderRepository.save(order);
    }
}
