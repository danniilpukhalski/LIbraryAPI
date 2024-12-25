package com.modsen.bookstorageservice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "book_view")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = false)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "2")
    Long id;


    @Schema(description = "ISBN", example = "213-345-677")
    String isbn;

    @Schema(description = "title", example = "A Clockwork Orange")
    String title;

    @Schema(description = "genre", example = "Fantasy")
    String genre;

    @Schema(description = "description", example = "about")
    String description;

    @Schema(description = "author", example = "J. K. Rowling")
    String author;

}