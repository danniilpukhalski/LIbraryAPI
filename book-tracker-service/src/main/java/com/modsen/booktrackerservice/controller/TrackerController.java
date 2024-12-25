package com.modsen.booktrackerservice.controller;


import com.modsen.booktrackerservice.service.TrackerService;
import com.modsen.booktrackerservice.dto.TrackerDto;
import com.modsen.booktrackerservice.dto.CreateTrackerRequest;
import com.modsen.booktrackerservice.dto.trackerStatusRequest;
import com.modsen.booktrackerservice.dto.validation.OnUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/book-track")
@RequiredArgsConstructor
@Validated
public class TrackerController {

    private final TrackerService trackerService;

    @Transactional
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public TrackerDto getTrackerById(@PathVariable Long id) {

        return trackerService.getTrackerById(id);
    }

    @Transactional
    @GetMapping("/get-by-book-id/{bookId}")
    @PreAuthorize("hasRole('USER')")
    public TrackerDto getTrackerByBookId(@PathVariable Long bookId) {
        return trackerService.getTrackerByBookId(bookId);
    }

    @Transactional
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER')")
    public List<TrackerDto> getAllTrackers() {

        return trackerService.getAllTrackers();
    }

    @Transactional
    @GetMapping("/get-all/free")
    @PreAuthorize("hasRole('USER')")
    public List<TrackerDto> getAllTrackersWhereStatusIsFree() {

        return trackerService.getTrackersWhereStatusIsFree();
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTrackerById(@PathVariable Long id) {
        trackerService.deleteTrackerById(id);

    }

    @Transactional
    @DeleteMapping("/delete-by-book-id/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTrackerBookById(@PathVariable Long bookId) {

        trackerService.deleteTrackerByBookId(bookId);
    }

    @Transactional
    @PutMapping("/update-status/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public TrackerDto updateTrackerStatus(@RequestBody @Valid trackerStatusRequest trackerStatusRequest, @PathVariable("bookId") Long bookId) {
        return trackerService.updateTrackerStatus(bookId, trackerStatusRequest);
    }

    @Transactional
    @PutMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public TrackerDto updateTracker(@Validated(OnUpdate.class) @RequestBody @Valid TrackerDto trackerDto) {
        trackerService.updateTracker(trackerDto);
        return trackerDto;
    }

    @Transactional
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public TrackerDto createTracker(@NotNull @RequestBody CreateTrackerRequest createTrackerRequest) {

        return trackerService.createTracker(createTrackerRequest.getBookId());
    }

}
