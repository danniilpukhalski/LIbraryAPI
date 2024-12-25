package com.modsen.bookstorageservice.web.dto;

import com.modsen.bookstorageservice.web.dto.validation.OnCreate;
import com.modsen.bookstorageservice.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDto {

    @NotNull(message = "Id must be non null", groups = OnUpdate.class)
    Long id;

    @NotEmpty(message = "ISBN muns be not null", groups = {OnUpdate.class, OnCreate.class})
    String isbn;

    @NotEmpty(message = "Title must be not null", groups = {OnUpdate.class, OnCreate.class})
    String title;

    @NotEmpty(message = "Genre must be not null", groups = {OnUpdate.class, OnCreate.class})
    String genre;

    @NotEmpty(message = "description mus be not null", groups = {OnUpdate.class, OnCreate.class})
    String description;

    @NotEmpty(message = "Author mus not null", groups = {OnUpdate.class, OnCreate.class})
    String author;
}
