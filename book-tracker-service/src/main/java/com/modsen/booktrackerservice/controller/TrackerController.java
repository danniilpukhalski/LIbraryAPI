package com.modsen.booktrackerservice.controller;


import com.modsen.booktrackerservice.service.TrackerService;
import com.modsen.booktrackerservice.dto.TrackerDto;
import com.modsen.booktrackerservice.dto.CreateTrackerRequest;
import com.modsen.booktrackerservice.dto.TrackerStatusRequest;
import com.modsen.booktrackerservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(description = "Tracker controller",name = "TrackerAPI")
public class TrackerController {

    private final TrackerService trackerService;

    @Operation(summary = "Get tracker by id")
    @Transactional
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public TrackerDto getTrackerById(@PathVariable Long id) {

        return trackerService.getTrackerById(id);
    }

    @Operation(summary = "Get tracker by book id")
    @Transactional
    @GetMapping("/get-by-book-id/{bookId}")
    @PreAuthorize("hasRole('USER')")
    public TrackerDto getTrackerByBookId(@PathVariable Long bookId) {
        return trackerService.getTrackerByBookId(bookId);
    }

    @Operation(summary = "Get all trackers")
    @Transactional
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER')")
    public List<TrackerDto> getAllTrackers() {

        return trackerService.getAllTrackers();
    }

    @Operation(summary = "Get all free trackers")
    @Transactional
    @GetMapping("/get-all/free")
    @PreAuthorize("hasRole('USER')")
    public List<TrackerDto> getAllTrackersWhereStatusIsFree() {

        return trackerService.getTrackersWhereStatusIsFree();
    }

    @Operation(summary = "Delete tracker by id")
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTrackerById(@PathVariable Long id) {
        trackerService.deleteTrackerById(id);

    }

    @Operation(summary = "Delete tracker by book id")
    @Transactional
    @DeleteMapping("/delete-by-book-id/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTrackerBookById(@PathVariable Long bookId) {

        trackerService.deleteTrackerByBookId(bookId);
    }

    @Operation(summary = "Update tracker status")
    @Transactional
    @PutMapping("/update-status/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public TrackerDto updateTrackerStatus(@RequestBody @Valid TrackerStatusRequest trackerStatusRequest, @PathVariable("bookId") Long bookId) {
        return trackerService.updateTrackerStatus(bookId, trackerStatusRequest);
    }

    @Operation(summary = "update tracker")
    @Transactional
    @PutMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public TrackerDto updateTracker(@Validated(OnUpdate.class) @RequestBody @Valid TrackerDto trackerDto) {
        trackerService.updateTracker(trackerDto);
        return trackerDto;
    }

    @Operation(summary = "Create tracker")
    @Transactional
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public TrackerDto createTracker(@NotNull @RequestBody CreateTrackerRequest createTrackerRequest) {

        return trackerService.createTracker(createTrackerRequest.getBookId());
    }

}
