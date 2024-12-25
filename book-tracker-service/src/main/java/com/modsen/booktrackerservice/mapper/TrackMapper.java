package com.modsen.booktrackerservice.mapper;


import com.modsen.booktrackerservice.domain.Tracker;
import com.modsen.booktrackerservice.dto.CreateTrackerRequest;
import com.modsen.booktrackerservice.dto.TrackerDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackMapper {

    TrackerDto toTrackerDto(Tracker tracker);

    Tracker toEntity(TrackerDto trackerDto);

    List<TrackerDto> toTrackerDto(List<Tracker> trackers);

    Tracker toEntity(CreateTrackerRequest createTrackerRequest);
}
