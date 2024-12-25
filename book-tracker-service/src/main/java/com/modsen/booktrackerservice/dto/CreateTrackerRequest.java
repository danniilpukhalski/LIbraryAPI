package com.modsen.booktrackerservice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTrackerRequest {

    @NotNull
    Long bookId;
}
