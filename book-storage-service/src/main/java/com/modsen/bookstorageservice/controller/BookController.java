package com.modsen.bookstorageservice.controller;


import com.modsen.bookstorageservice.service.BookService;
import com.modsen.bookstorageservice.dto.BookDto;
import com.modsen.bookstorageservice.dto.validation.OnCreate;
import com.modsen.bookstorageservice.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Book controller",description = "BookAPI")
public class BookController {

    private final BookService bookService;

    @Transactional
    @PutMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update book")
    public BookDto updateBook(@Validated(OnUpdate.class) @RequestBody BookDto bookDto) {
        return bookService.updateBook(bookDto);
    }

    @Transactional
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get book by id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Transactional
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all books")
    public List<BookDto> getAllBooks() {

        return bookService.getAllBooks();
    }

    @Transactional
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create book")
    public BookDto createBook(@Validated(OnCreate.class) @RequestBody BookDto bookDto) {
        return bookService.createBook(bookDto);
    }

    @Transactional
    @GetMapping("/isbn/{isbn}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get book by isbn")
    public BookDto getBookByIsbn(@PathVariable String isbn) {

        return bookService.getBookByIsbn(isbn);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete book by id")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

}
