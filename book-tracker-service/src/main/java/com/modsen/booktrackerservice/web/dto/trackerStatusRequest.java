package com.modsen.booktrackerservice.web.dto;


import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class trackerStatusRequest {

    @Pattern(regexp = "^(free|taken)$",message = "status must be fre or taken")
    private String status;

}
