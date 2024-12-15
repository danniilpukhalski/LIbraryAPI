package com.modsen.bookstorageservice.web.dto;

import com.modsen.bookstorageservice.web.dto.validation.OnCreate;
import com.modsen.bookstorageservice.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @NotNull(message = "Id must be non null", groups = OnUpdate.class)
    private long id;

    @NotEmpty(message = "ISBN muns be not null",groups = {OnUpdate.class, OnCreate.class})
    private String isbn;

    @NotEmpty(message = "Title must be not null",groups = {OnUpdate.class, OnCreate.class})
    private String title;

    @NotEmpty(message = "Genre must be not null",groups = {OnUpdate.class, OnCreate.class})
    private String genre;

    @NotEmpty(message = "description mus be not null",groups = {OnUpdate.class, OnCreate.class})
    private String description;

    @NotEmpty(message = "Author mus not null",groups = {OnUpdate.class, OnCreate.class})
    private String author;
}
