package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.service.RabbitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RabbitServiceImpl implements RabbitService {

    RabbitTemplate rabbitTemplate;
    DirectExchange directExchange;

    @Override
    public void addCreateBook(Long bookId) {
        String message = "create," + bookId;
        log.info("Sending message to 'create_book' queue for creating book with ID: {}", bookId);
        try {
            rabbitTemplate.convertAndSend(directExchange.getName(), "create_book", message);
            log.info("Message for creating book with ID: {} successfully sent", bookId);
        } catch (Exception e) {
            log.error("Error sending message for creating book with ID: {}", bookId, e);
        }
    }

    @Override
    public void addDeleteBook(Long bookId) {
        String message = "delete," + bookId;
        log.info("Sending message to 'delete_book' queue for deleting book with ID: {}", bookId);
        try {
            rabbitTemplate.convertAndSend(directExchange.getName(), "delete_book", message);
            log.info("Message for deleting book with ID: {} successfully sent", bookId);
        } catch (Exception e) {
            log.error("Error sending message for deleting book with ID: {}", bookId, e);
        }
    }
}
