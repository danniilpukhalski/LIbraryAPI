package com.modsen.booktrackerservice.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "book_track_view")
public class Tracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "1")
    private Long id;


    @Column(name = "bookid")
    @Schema(description = "bookId", example = "2")
    private Long bookId;

    @Pattern(regexp = "^(free|taken)$", message = "status must be free or taken")
    @Schema(description = "status", example = "free")
    private String status;

    @Schema(description = "taken", example = "13-12-2024")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate taken;

    @Schema(description = "returned", example = "14-12-2024")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate returned;

    @Schema(description = "isDeleted", example = "true")
    private Boolean deleted;
}
