package com.modsen.booktrackerservice.service.impl;

import com.modsen.booktrackerservice.domain.Tracker;
import com.modsen.booktrackerservice.domain.exception.DuplicateResourceException;
import com.modsen.booktrackerservice.domain.exception.ResourceNotFoundException;
import com.modsen.booktrackerservice.repository.TrackerRepository;
import com.modsen.booktrackerservice.service.TrackerService;
import com.modsen.booktrackerservice.web.dto.TrackerDto;
import com.modsen.booktrackerservice.web.dto.trackerStatusRequest;
import com.modsen.booktrackerservice.web.mapper.TrackMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Validated
public class TrackerServiceImpl implements TrackerService {

    private final TrackerRepository trackerRepository;
    private final TrackMapper trackMapper;


    @Override
    public TrackerDto getTrackerByBookId(Long bookId) {
        return trackMapper.toTrackerDto(trackerRepository.findTrackerByBookId(bookId).orElseThrow(() ->
                new ResourceNotFoundException("Tracker not found")));

    }


    @Override
    public TrackerDto getTrackerById(Long id) {
        return trackMapper.toTrackerDto(trackerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tracker not found")));
    }

    @Override
    public List<TrackerDto> getTrackersWhereStatusIsFree() {

        return trackMapper.toTrackerDto(trackerRepository.findTrackerByStatus("free"));
    }

    @Override
    public TrackerDto updateTrackerStatus(Long bookId, trackerStatusRequest trackerStatusRequest) {
        Tracker tracker = trackerRepository.findTrackerByBookId(bookId).orElseThrow(() ->
                new ResourceNotFoundException("Tracker not found"));
        assert tracker != null;
        if (trackerStatusRequest.getStatus().equals("taken")) {
            tracker.setTaken(LocalDate.now());
        } else {
            tracker.setReturned(LocalDate.now());
        }
        tracker.setStatus(trackerStatusRequest.getStatus());
        trackerRepository.save(tracker);
        return trackMapper.toTrackerDto(tracker);
    }

    @Override
    public List<TrackerDto> getAllTrackers() {

        return trackMapper.toTrackerDto(trackerRepository.findAll());
    }

    @Override
    public TrackerDto createTracker(Long bookId) {
        if (trackerRepository.findById(bookId).isPresent()) {
            throw new DuplicateResourceException("Tracker already exists");
        }
        Tracker tracker = new Tracker();
        tracker.setBookId(bookId);
        tracker.setStatus("free");
        trackerRepository.save(tracker);
        return trackMapper.toTrackerDto(tracker);
    }

    @Override
    public TrackerDto updateTracker(TrackerDto trackerDto) {
        trackerRepository.findById(trackerDto.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Tracker not found"));
        trackerRepository.save(trackMapper.toEntity(trackerDto));
        return trackerDto;
    }

    @Override
    public void softDeleteTrackerById(Long id) {
        if (!trackerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tracker not found");
        }
        trackerRepository.deleteById(id);

    }

    @Override
    public void softDeleteTrackerByBookId(Long bookId) {
        Tracker tracker = trackerRepository.findTrackerByBookId(bookId).orElseThrow(() ->
                new ResourceNotFoundException("Tracker not found"));
        tracker.setDeleted(true);
        trackerRepository.save(tracker);
    }

    @RabbitListener(queues = "create_book_queue")
    public void receiveCreateMessage(String message) {
        String[] parts = message.split(",");
        String action = parts[0];
        Long bookId = Long.parseLong(parts[1]);
        if ("create".equals(action)) {
            createTracker(bookId);
        }
    }

    @RabbitListener(queues = "delete_book_queue")
    public void receiveSoftDeleteMessage(String message) {
        String[] parts = message.split(",");
        String action = parts[0];
        Long bookId = Long.parseLong(parts[1]);
        if ("delete".equals(action)) {
            softDeleteTrackerByBookId(bookId);
        }
    }

}
