package com.modsen.booktrackerservice.web.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTrackerRequest {

    @NotNull
    private Long bookId;
}
