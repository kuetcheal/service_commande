package com.javathinked.example.demo_spring.service;

import com.javathinked.example.demo_spring.dto.OrderProductDto;
import com.javathinked.example.demo_spring.model.Order;
import com.javathinked.example.demo_spring.model.OrderProduct;
import com.javathinked.example.demo_spring.repository.OrderProductRepository;
import com.javathinked.example.demo_spring.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
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
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found: " + dto.getOrderId()));

        OrderProduct entity = new OrderProduct(order, dto.getProductId());
        return orderProductRepository.save(entity);
    }

    @Override
    public OrderProduct saveOrderProduct(OrderProduct orderProduct) {
        // NOTE: si tu appelles cette méthode directement, vérifie que orderProduct.getOrder()
        // n'est pas null, sinon préfère createFromDto(dto) pour passer par le mapping.
        if (orderProduct.getOrder() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order is required");
        }
        return orderProductRepository.save(orderProduct);
    }

    @Override
    public List<OrderProduct> getAllOrderProducts() {
        return orderProductRepository.findAll();
    }

    @Override
    public OrderProduct getOrderProductById(Long id) {
        return orderProductRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "OrderProduct not found: " + id));
    }

    @Override
    public void deleteOrderProduct(Long id) {
        if (!orderProductRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "OrderProduct not found: " + id);
        }
        orderProductRepository.deleteById(id);
    }
}
