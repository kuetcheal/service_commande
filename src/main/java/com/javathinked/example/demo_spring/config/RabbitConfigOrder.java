package com.javathinked.example.demo_spring.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfigOrder {

    public static final String EXCHANGE = "app.exchange";

    // Queues écoutées par le service commande :
    public static final String Q_FROM_PRODUCT_STOCK = "q.order.from-product-stock";
    public static final String Q_FROM_CLIENT        = "q.order.from-client";

    @Bean
    TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue qFromProductStock() {
        return new Queue(Q_FROM_PRODUCT_STOCK, true);
    }

    @Bean
    Queue qFromClient() {
        return new Queue(Q_FROM_CLIENT, true);
    }

    @Bean
    Binding bindProductStock(Queue qFromProductStock, TopicExchange appExchange) {
        return BindingBuilder.bind(qFromProductStock).to(appExchange).with("product.stock.*");
    }

    @Bean
    Binding bindClient(Queue qFromClient, TopicExchange appExchange) {
        return BindingBuilder.bind(qFromClient).to(appExchange).with("client.*");
    }

    // JSON converter + template
    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter c) {
        RabbitTemplate t = new RabbitTemplate(cf);
        t.setMessageConverter(c);
        return t;
    }
}
