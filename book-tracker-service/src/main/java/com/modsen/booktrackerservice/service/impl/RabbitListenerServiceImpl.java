package com.modsen.booktrackerservice.service.impl;

import com.modsen.booktrackerservice.service.RabbitListenerService;
import com.modsen.booktrackerservice.service.TrackerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RabbitListenerServiceImpl implements RabbitListenerService {

    TrackerService trackerService;

    @Override
    @RabbitListener(queues = "create_book_queue")
    public void receiveCreateMessage(String message) {
        String[] parts = message.split(",");
        String action = parts[0];
        Long bookId = Long.parseLong(parts[1]);
        if ("create".equals(action)) {
            trackerService.createTracker(bookId);
        }
    }

    @Override
    @RabbitListener(queues = "delete_book_queue")
    public void receiveDeleteMessage(String message) {
        String[] parts = message.split(",");
        String action = parts[0];
        Long bookId = Long.parseLong(parts[1]);
        if ("delete".equals(action)) {
            trackerService.deleteTrackerByBookId(bookId);
        }
    }
}
