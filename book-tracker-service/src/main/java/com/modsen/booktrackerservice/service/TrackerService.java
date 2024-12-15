package com.modsen.booktrackerservice.service;


import com.modsen.booktrackerservice.web.dto.TrackerDto;
import com.modsen.booktrackerservice.web.dto.trackerStatusRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TrackerService {


    TrackerDto getTrackerByBookId(Long bookId);

    TrackerDto getTrackerById(Long id);

    List<TrackerDto> getTrackersWhereStatusIsFree();

    TrackerDto updateTrackerStatus(Long bookId, trackerStatusRequest trackerStatusRequest);

    List<TrackerDto> getAllTrackers();

    TrackerDto createTracker(Long id);

    TrackerDto updateTracker(TrackerDto trackerDto);

    void softDeleteTrackerById(Long Id);

    void softDeleteTrackerByBookId(Long bookId);
}
