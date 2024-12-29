package com.modsen.booktrackerservice.service.impl;

import com.modsen.booktrackerservice.service.RabbitListenerService;
import com.modsen.booktrackerservice.service.TrackerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class RabbitListenerServiceImpl implements RabbitListenerService {

    TrackerService trackerService;

    @Override
    @RabbitListener(queues = "create_book_queue")
    public void receiveCreateMessage(String message) {
        log.info("Received message from 'create_book_queue': {}", message);

        String[] parts = message.split(",");
        String action = parts[0];
        Long bookId = Long.parseLong(parts[1]);

        if ("create".equals(action)) {
            log.info("Action 'create' received for bookId: {}", bookId);
            try {
                trackerService.createTracker(bookId);
                log.info("Tracker successfully created for bookId: {}", bookId);
            } catch (Exception e) {
                log.error("Failed to create tracker for bookId: {}. Error: {}", bookId, e.getMessage());
            }
        } else {
            log.warn("Received unknown action: {} for bookId: {}", action, bookId);
        }
    }

    @Override
    @RabbitListener(queues = "delete_book_queue")
    public void receiveDeleteMessage(String message) {
        log.info("Received message from 'delete_book_queue': {}", message);

        String[] parts = message.split(",");
        String action = parts[0];
        Long bookId = Long.parseLong(parts[1]);

        if ("delete".equals(action)) {
            log.info("Action 'delete' received for bookId: {}", bookId);
            try {
                trackerService.deleteTrackerByBookId(bookId);
                log.info("Tracker successfully deleted for bookId: {}", bookId);
            } catch (Exception e) {
                log.error("Failed to delete tracker for bookId: {}. Error: {}", bookId, e.getMessage());
            }
        } else {
            log.warn("Received unknown action: {} for bookId: {}", action, bookId);
        }
    }
}
