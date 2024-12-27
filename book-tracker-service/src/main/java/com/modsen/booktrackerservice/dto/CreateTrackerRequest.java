package com.modsen.booktrackerservice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTrackerRequest {

    @NotNull
    @Schema(description="bookId",example="11")
    Long bookId;
}
