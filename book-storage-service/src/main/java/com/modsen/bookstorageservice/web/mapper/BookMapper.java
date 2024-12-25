package com.modsen.bookstorageservice.web.mapper;


import com.modsen.bookstorageservice.domain.Book;
import com.modsen.bookstorageservice.web.dto.BookDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(BookDto bookDto);

    List<BookDto> toDto(List<Book> books);
}


