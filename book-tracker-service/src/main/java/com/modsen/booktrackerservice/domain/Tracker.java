package com.modsen.booktrackerservice.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "book_track_view")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "1")
    Long id;


    @Column(name = "bookid")
    @Schema(description = "bookId", example = "2")
    Long bookId;

    @Pattern(regexp = "^(free|taken)$", message = "status must be free or taken")
    @Schema(description = "status", example = "free")
    String status;

    @Schema(description = "taken", example = "13-12-2024")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate taken;

    @Schema(description = "returned", example = "14-12-2024")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate returned;

    @Schema(description = "isDeleted", example = "true")
    Boolean deleted;
}
