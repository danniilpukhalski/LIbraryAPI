package com.modsen.booktrackerservice.web.mapper;


import com.modsen.booktrackerservice.domain.Tracker;
import com.modsen.booktrackerservice.web.dto.TrackerDto;
import com.modsen.booktrackerservice.web.dto.CreateTrackerRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackMapper {

TrackerDto toTrackerDto(Tracker tracker);

Tracker toEntity(TrackerDto trackerDto);

List<TrackerDto> toTrackerDto(List<Tracker> trackers);

Tracker toEntity(CreateTrackerRequest createTrackerRequest);
}
