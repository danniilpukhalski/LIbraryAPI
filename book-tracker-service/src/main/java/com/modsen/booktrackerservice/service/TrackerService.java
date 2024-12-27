package com.modsen.booktrackerservice.service;


import com.modsen.booktrackerservice.dto.TrackerDto;
import com.modsen.booktrackerservice.dto.TrackerStatusRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrackerService {


    TrackerDto getTrackerByBookId(Long bookId);

    TrackerDto getTrackerById(Long id);

    List<TrackerDto> getTrackersWhereStatusIsFree();

    TrackerDto updateTrackerStatus(Long bookId, TrackerStatusRequest trackerStatusRequest);

    List<TrackerDto> getAllTrackers();

    TrackerDto createTracker(Long id);

    TrackerDto updateTracker(TrackerDto trackerDto);

    void deleteTrackerById(Long Id);

    void deleteTrackerByBookId(Long bookId);
}
