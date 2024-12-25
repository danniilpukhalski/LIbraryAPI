package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.service.RabbitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RabbitServiceImpl implements RabbitService {

    RabbitTemplate rabbitTemplate;
    DirectExchange directExchange;

    @Override
    public void addCreateBook(Long bookId) {
        rabbitTemplate.convertAndSend(directExchange.getName(), "create_book", "create," + bookId);
    }

    @Override
    public void addDeleteBook(Long bookId) {
        rabbitTemplate.convertAndSend(directExchange.getName(), "delete_book", "delete," + bookId);
    }
}
