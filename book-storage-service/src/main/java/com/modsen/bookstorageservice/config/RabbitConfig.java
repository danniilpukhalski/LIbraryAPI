package com.modsen.bookstorageservice.config;


import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RabbitConfig {

    @Bean
    public Queue deleteBookQueue() {
        log.info("Creating 'delete_book_queue' for book deletion requests");
        return new Queue("delete_book_queue");
    }

    @Bean
    public Queue createBookQueue() {
        log.info("Creating 'create_book_queue' for book creation requests");
        return new Queue("create_book_queue");
    }

    @Bean
    public DirectExchange bookExchange() {
        log.info("Creating 'book_exchange' for routing book-related messages");
        return new DirectExchange("book_exchange");
    }

    @Bean
    public Binding deleteBookBinding() {
        log.info("Binding 'delete_book_queue' to 'book_exchange' with routing key 'delete_book'");
        return BindingBuilder.bind(deleteBookQueue())
                .to(bookExchange())
                .with("delete_book");
    }

    @Bean
    public Binding createBookBinding() {
        log.info("Binding 'create_book_queue' to 'book_exchange' with routing key 'create_book'");
        return BindingBuilder.bind(createBookQueue())
                .to(bookExchange())
                .with("create_book");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        log.info("Creating RabbitAdmin bean to manage RabbitMQ resources");
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public CommandLineRunner setupQueues(RabbitAdmin rabbitAdmin) {
        return args -> {
            log.info("Registering queues, exchanges, and bindings in RabbitMQ...");
            rabbitAdmin.declareQueue(deleteBookQueue());
            rabbitAdmin.declareQueue(createBookQueue());
            rabbitAdmin.declareExchange(bookExchange());
            rabbitAdmin.declareBinding(deleteBookBinding());
            rabbitAdmin.declareBinding(createBookBinding());
            log.info("Queues, exchanges, and bindings successfully registered.");
        };
    }
}

