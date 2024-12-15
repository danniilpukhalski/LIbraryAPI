package com.modsen.bookstorageservice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "book_view")
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id", example = "2")
    private Long id;


    @Schema(description = "ISBN", example = "213-345-677")
    private String isbn;

    @Schema(description = "title", example = "A Clockwork Orange")
    private String title;

    @Schema(description = "genre", example = "Fantasy")
    private String genre;

    @Schema(description = "description", example = "about")
    private String description;

    @Schema(description = "author", example = "J. K. Rowling")
    private String author;

    @Schema(description = "Is deleted",example = "true")
    private Boolean deleted;

}