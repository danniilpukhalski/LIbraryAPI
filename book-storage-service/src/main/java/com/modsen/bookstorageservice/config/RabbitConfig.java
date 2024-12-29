package com.modsen.bookstorageservice.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Bean
    public Queue deleteBookQueue() {

        return new Queue("delete_book_queue");
    }

    @Bean
    public Queue createBookQueue() {

        return new Queue("create_book_queue");
    }

    @Bean
    public DirectExchange bookExchange() {

        return new DirectExchange("book_exchange");
    }

    @Bean
    public Binding deleteBookBinding() {
        return BindingBuilder.bind(deleteBookQueue())
                .to(bookExchange())
                .with("delete_book");
    }

    @Bean
    public Binding createBookBinding() {
        return BindingBuilder.bind(createBookQueue())
                .to(bookExchange())
                .with("create_book");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public CommandLineRunner setupQueues(RabbitAdmin rabbitAdmin) {
        return args -> {
            System.out.println("Registering queues and bindings...");
            rabbitAdmin.declareQueue(deleteBookQueue());
            rabbitAdmin.declareQueue(createBookQueue());
            rabbitAdmin.declareExchange(bookExchange());
            rabbitAdmin.declareBinding(deleteBookBinding());
            rabbitAdmin.declareBinding(createBookBinding());
        };
    }
}

