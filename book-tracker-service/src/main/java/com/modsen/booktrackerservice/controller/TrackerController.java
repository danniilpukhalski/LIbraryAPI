package com.modsen.booktrackerservice.controller;


import com.modsen.booktrackerservice.dto.CreateTrackerRequest;
import com.modsen.booktrackerservice.dto.TrackerDto;
import com.modsen.booktrackerservice.dto.TrackerStatusRequest;
import com.modsen.booktrackerservice.dto.validation.OnUpdate;
import com.modsen.booktrackerservice.service.TrackerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/book-track")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
@Tag(description = "Tracker controller", name = "TrackerAPI")
@Slf4j
public class TrackerController {

    private final TrackerService trackerService;

    @Operation(summary = "Get tracker by id")
    @Transactional
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public TrackerDto getTrackerById(@PathVariable Long id) {
        log.info("Attempting to retrieve tracker with ID: {}", id);
        TrackerDto tracker = trackerService.getTrackerById(id);
        if (tracker != null) {
            log.info("Tracker with ID: {} retrieved successfully.", id);
        } else {
            log.error("Tracker with ID: {} not found.", id);
        }
        return tracker;
    }

    @Operation(summary = "Get tracker by book id")
    @Transactional
    @GetMapping("/get-by-book-id/{bookId}")
    @PreAuthorize("hasRole('USER')")
    public TrackerDto getTrackerByBookId(@PathVariable Long bookId) {
        log.info("Attempting to retrieve tracker with book ID: {}", bookId);
        TrackerDto tracker = trackerService.getTrackerByBookId(bookId);
        if (tracker != null) {
            log.info("Tracker with book ID: {} retrieved successfully.", bookId);
        } else {
            log.error("Tracker with book ID: {} not found.", bookId);
        }
        return tracker;
    }

    @Operation(summary = "Get all trackers")
    @Transactional
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER')")
    public List<TrackerDto> getAllTrackers() {
        log.info("Attempting to retrieve all trackers.");
        List<TrackerDto> trackers = trackerService.getAllTrackers();
        log.info("Retrieved {} trackers.", trackers.size());
        return trackers;
    }

    @Operation(summary = "Get all free trackers")
    @Transactional
    @GetMapping("/get-all/free")
    @PreAuthorize("hasRole('USER')")
    public List<TrackerDto> getAllTrackersWhereStatusIsFree() {
        log.info("Attempting to retrieve all free trackers.");
        List<TrackerDto> freeTrackers = trackerService.getTrackersWhereStatusIsFree();
        log.info("Retrieved {} free trackers.", freeTrackers.size());
        return freeTrackers;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete tracker by id")
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTrackerById(@PathVariable Long id) {
        log.info("Attempting to delete tracker with ID: {}", id);
        trackerService.deleteTrackerById(id);
        log.info("Tracker with ID: {} deleted successfully.", id);
    }

    @Operation(summary = "Delete tracker by book id")
    @Transactional
    @DeleteMapping("/delete-by-book-id/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTrackerBookById(@PathVariable Long bookId) {
        log.info("Attempting to delete tracker with book ID: {}", bookId);
        trackerService.deleteTrackerByBookId(bookId);
        log.info("Tracker with book ID: {} deleted successfully.", bookId);
    }

    @Operation(summary = "Update tracker status")
    @Transactional
    @PutMapping("/update-status/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public TrackerDto updateTrackerStatus(@RequestBody @Valid TrackerStatusRequest trackerStatusRequest, @PathVariable("bookId") Long bookId) {
        log.info("Attempting to update status of tracker with book ID: {}", bookId);
        TrackerDto updatedTracker = trackerService.updateTrackerStatus(bookId, trackerStatusRequest);
        log.info("Tracker with book ID: {} updated successfully with new status.", bookId);
        return updatedTracker;
    }

    @Operation(summary = "Update tracker")
    @Transactional
    @PutMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public TrackerDto updateTracker(@Validated(OnUpdate.class) @RequestBody @Valid TrackerDto trackerDto) {
        log.info("Attempting to update tracker with ID: {}", trackerDto.getId());
        trackerService.updateTracker(trackerDto);
        log.info("Tracker with ID: {} updated successfully.", trackerDto.getId());
        return trackerDto;
    }

    @Operation(summary = "Create tracker")
    @Transactional
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public TrackerDto createTracker(@NotNull @RequestBody CreateTrackerRequest createTrackerRequest) {
        log.info("Attempting to create a new tracker for book ID: {}", createTrackerRequest.getBookId());
        TrackerDto newTracker = trackerService.createTracker(createTrackerRequest.getBookId());
        log.info("New tracker created successfully for book ID: {}", createTrackerRequest.getBookId());
        return newTracker;
    }

}
