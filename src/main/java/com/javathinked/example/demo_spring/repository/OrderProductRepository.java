package com.javathinked.example.demo_spring.repository;

import com.javathinked.example.demo_spring.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByOrder_Id(Long orderId);

    @Query("select coalesce(sum(op.unitPriceSnapshot * op.quantity), 0) " +
           "from OrderProduct op where op.order.id = :orderId")
    BigDecimal sumTotalByOrderId(@Param("orderId") Long orderId);
}
