package com.javathinked.example.demo_spring.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfigOrder {

    public static final String EXCHANGE = "app.exchange";

    // Queues écoutées par le service COMMANDE
    public static final String Q_FROM_PRODUCT_STOCK = "q.order.from-product-stock";
    public static final String Q_FROM_CLIENT        = "q.order.from-client";

    // Routings
    public static final String RK_PRODUCT_STOCK_ALL = "product.stock.*";
    public static final String RK_CLIENT_ALL        = "client.*";

    @Bean
    TopicExchange appExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean(name = "qOrderFromProductStock")
    Queue qFromProductStock() {
        return new Queue(Q_FROM_PRODUCT_STOCK, true);
    }

    @Bean(name = "qOrderFromClient")
    Queue qFromClient() {
        return new Queue(Q_FROM_CLIENT, true);
    }

    @Bean
    Binding bindProductStock(@Qualifier("qOrderFromProductStock") Queue q, TopicExchange appExchange) {
        return BindingBuilder.bind(q).to(appExchange).with(RK_PRODUCT_STOCK_ALL);
    }

    @Bean
    Binding bindClient(@Qualifier("qOrderFromClient") Queue q, TopicExchange appExchange) {
        return BindingBuilder.bind(q).to(appExchange).with(RK_CLIENT_ALL);
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
