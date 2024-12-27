package com.modsen.booktrackerservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrackerStatusRequest {

    @Pattern(regexp = "^(free|taken)$", message = "status must be fre or taken")
    @Schema(description = "tracker status",example = "true")
    String status;

}
