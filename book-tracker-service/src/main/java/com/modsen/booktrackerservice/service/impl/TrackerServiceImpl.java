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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrackerServiceImpl implements TrackerService {

    TrackerRepository trackerRepository;
    TrackMapper trackMapper;


    @Override
    public TrackerDto getTrackerByBookId(Long bookId) {
        return trackMapper.toTrackerDto(trackerRepository.findTrackerByBookId(bookId).orElseThrow(() ->
                new ResourceNotFoundException("Tracker with bookId " + bookId + " not found")));

    }


    @Override
    public TrackerDto getTrackerById(Long id) {
        return trackMapper.toTrackerDto(trackerRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tracker with id " + id + " not found")));
    }

    @Override
    public List<TrackerDto> getTrackersWhereStatusIsFree() {

        return trackMapper.toTrackerDto(trackerRepository.findTrackerByStatus("free"));
    }

    @Override
    public TrackerDto updateTrackerStatus(Long bookId, TrackerStatusRequest trackerStatusRequest) {
        Tracker tracker = trackerRepository.findTrackerByBookId(bookId).orElseThrow(() ->
                new ResourceNotFoundException("Tracker with bookId " + bookId + " not found"));
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
            throw new DuplicateResourceException("Tracker with bookId " + bookId + " already exists");
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
                new ResourceNotFoundException("Tracker with id " + trackerDto.getId() + " not found"));
        trackerRepository.save(trackMapper.toEntity(trackerDto));
        return trackerDto;
    }

    @Override
    public void deleteTrackerById(Long id) {
        if (!trackerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tracker with id " + id + " not found");
        }
        trackerRepository.deleteById(id);

    }

    @Override
    public void deleteTrackerByBookId(Long bookId) {
        Tracker tracker = trackerRepository.findTrackerByBookId(bookId).orElseThrow(() ->
                new ResourceNotFoundException("Tracker with id " + bookId + " not found"));
        trackerRepository.delete(tracker);
    }

}
