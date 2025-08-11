package com.javathinked.example.demo_spring.messaging;

import com.javathinked.example.demo_spring.config.RabbitConfigOrder;
import com.javathinked.example.demo_spring.dto.events.ClientCreatedEvent;
import com.javathinked.example.demo_spring.dto.events.ProductStockUpdatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumers {

    @RabbitListener(queues = RabbitConfigOrder.Q_FROM_PRODUCT_STOCK)
    public void onProductStockUpdated(ProductStockUpdatedEvent evt) {
        // ex : vérifier panier / stock
        System.out.println("OrderService <- Product stock updated: " + evt);
    }

    @RabbitListener(queues = RabbitConfigOrder.Q_FROM_CLIENT)
    public void onClientCreated(ClientCreatedEvent evt) {
        // ex : pré-créer profil client côté commande
        System.out.println("OrderService <- Client created: " + evt);
    }
}
