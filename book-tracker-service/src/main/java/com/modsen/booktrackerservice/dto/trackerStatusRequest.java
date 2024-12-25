package com.modsen.booktrackerservice.dto;


import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class trackerStatusRequest {

    @Pattern(regexp = "^(free|taken)$", message = "status must be fre or taken")
    String status;

}
