package com.modsen.booktrackerservice.service.impl;

import com.modsen.booktrackerservice.domain.Tracker;
import com.modsen.booktrackerservice.domain.exception.DuplicateResourceException;
import com.modsen.booktrackerservice.domain.exception.ResourceNotFoundException;
import com.modsen.booktrackerservice.dto.TrackerDto;
import com.modsen.booktrackerservice.dto.TrackerStatusRequest;
import com.modsen.booktrackerservice.mapper.TrackMapper;
import com.modsen.booktrackerservice.repository.TrackerRepository;
import com.modsen.booktrackerservice.service.TrackerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TrackerServiceImpl implements TrackerService {

    TrackerRepository trackerRepository;
    TrackMapper trackMapper;


    @Override
    public TrackerDto getTrackerByBookId(Long bookId) {
        log.info("Fetching tracker by bookId: {}", bookId);
        return trackMapper.toTrackerDto(trackerRepository.findTrackerByBookId(bookId)
                .orElseThrow(() -> {
                    log.error("Tracker with bookId: {} not found", bookId);
                    return new ResourceNotFoundException("Tracker with bookId " + bookId + " not found");
                }));
    }

    @Override
    public TrackerDto getTrackerById(Long id) {
        log.info("Fetching tracker by id: {}", id);
        return trackMapper.toTrackerDto(trackerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Tracker with id: {} not found", id);
                    return new ResourceNotFoundException("Tracker with id " + id + " not found");
                }));
    }

    @Override
    public List<TrackerDto> getTrackersWhereStatusIsFree() {
        log.info("Fetching all trackers where status is 'free'");
        return trackMapper.toTrackerDto(trackerRepository.findTrackerByStatus("free"));
    }

    @Override
    public TrackerDto updateTrackerStatus(Long bookId, TrackerStatusRequest trackerStatusRequest) {
        log.info("Updating tracker status for bookId: {} with status: {}", bookId, trackerStatusRequest.getStatus());
        Tracker tracker = trackerRepository.findTrackerByBookId(bookId)
                .orElseThrow(() -> {
                    log.error("Tracker with bookId: {} not found", bookId);
                    return new ResourceNotFoundException("Tracker with bookId " + bookId + " not found");
                });

        if (trackerStatusRequest.getStatus().equals("taken")) {
            tracker.setTaken(LocalDate.now());
        } else {
            tracker.setReturned(LocalDate.now());
        }
        tracker.setStatus(trackerStatusRequest.getStatus());
        trackerRepository.save(tracker);
        log.info("Tracker status for bookId: {} updated to {}", bookId, trackerStatusRequest.getStatus());
        return trackMapper.toTrackerDto(tracker);
    }

    @Override
    public List<TrackerDto> getAllTrackers() {
        log.info("Fetching all trackers");
        return trackMapper.toTrackerDto(trackerRepository.findAll());
    }

    @Override
    public TrackerDto createTracker(Long bookId) {
        log.info("Creating tracker for bookId: {}", bookId);
        if (trackerRepository.findById(bookId).isPresent()) {
            log.error("Tracker with bookId: {} already exists", bookId);
            throw new DuplicateResourceException("Tracker with bookId " + bookId + " already exists");
        }
        Tracker tracker = new Tracker();
        tracker.setBookId(bookId);
        tracker.setStatus("free");
        trackerRepository.save(tracker);
        log.info("Tracker created for bookId: {} with status 'free'", bookId);
        return trackMapper.toTrackerDto(tracker);
    }

    @Override
    public TrackerDto updateTracker(TrackerDto trackerDto) {
        log.info("Updating tracker with id: {}", trackerDto.getId());
        trackerRepository.findById(trackerDto.getId()).orElseThrow(() -> {
            log.error("Tracker with id: {} not found", trackerDto.getId());
            return new ResourceNotFoundException("Tracker with id " + trackerDto.getId() + " not found");
        });
        trackerRepository.save(trackMapper.toEntity(trackerDto));
        log.info("Tracker with id: {} updated", trackerDto.getId());
        return trackerDto;
    }

    @Override
    public void deleteTrackerById(Long id) {
        log.info("Deleting tracker with id: {}", id);
        if (!trackerRepository.existsById(id)) {
            log.error("Tracker with id: {} not found", id);
            throw new ResourceNotFoundException("Tracker with id " + id + " not found");
        }
        trackerRepository.deleteById(id);
        log.info("Tracker with id: {} deleted", id);
    }

    @Override
    public void deleteTrackerByBookId(Long bookId) {
        log.info("Deleting tracker by bookId: {}", bookId);
        Tracker tracker = trackerRepository.findTrackerByBookId(bookId)
                .orElseThrow(() -> {
                    log.error("Tracker with bookId: {} not found", bookId);
                    return new ResourceNotFoundException("Tracker with bookId " + bookId + " not found");
                });
        trackerRepository.delete(tracker);
        log.info("Tracker with bookId: {} deleted", bookId);
    }
}
