package com.modsen.bookstorageservice.service;

import com.modsen.bookstorageservice.web.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {

    BookDto getBookById(Long id);

    BookDto getBookByIsbn(String isbn);

    List<BookDto> getAllBooks();

    BookDto createBook(BookDto bookDto);

    BookDto updateBook(BookDto bookDto);

    void deleteBook(Long id);


}
