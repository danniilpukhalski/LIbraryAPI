package com.modsen.bookstorageservice.web.controller;


import com.modsen.bookstorageservice.domain.Book;
import com.modsen.bookstorageservice.service.BookService;
import com.modsen.bookstorageservice.web.dto.BookDto;
import com.modsen.bookstorageservice.web.dto.validation.OnCreate;
import com.modsen.bookstorageservice.web.dto.validation.OnUpdate;
import com.modsen.bookstorageservice.web.mapper.BookMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
@Validated
public class BookController {

    private final BookService bookService;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;

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
        BookDto createdBook = bookService.createBook(bookDto);
        rabbitTemplate.convertAndSend(directExchange.getName(), "create_book", "create," + createdBook.getId());
        return createdBook;
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
    public void softDeleteBook(@PathVariable Long id) {
        rabbitTemplate.convertAndSend(directExchange.getName(), "soft_delete_book_queue", "delete," + id);
        bookService.softDeleteBook(id);
    }

}
