package com.modsen.bookstorageservice.web.controller;


import com.modsen.bookstorageservice.service.BookService;
import com.modsen.bookstorageservice.web.dto.BookDto;
import com.modsen.bookstorageservice.web.dto.validation.OnCreate;
import com.modsen.bookstorageservice.web.dto.validation.OnUpdate;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    private final BookService bookService;

    @Transactional
    @PutMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto updateBook(@Validated(OnUpdate.class) @RequestBody BookDto bookDto) {
        return bookService.updateBook(bookDto);
    }

    @Transactional
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Transactional
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER')")
    public List<BookDto> getAllBooks() {

        return bookService.getAllBooks();
    }

    @Transactional
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto createBook(@Validated(OnCreate.class) @RequestBody BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @Transactional
    @GetMapping("/isbn/{isbn}")
    @PreAuthorize("hasRole('USER')")
    public BookDto getBookByIsbn(@PathVariable String isbn) {

        return bookService.getBookByIsbn(isbn);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void DeleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

}
