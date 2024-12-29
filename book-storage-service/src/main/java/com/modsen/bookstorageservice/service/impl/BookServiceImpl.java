package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.domain.Book;
import com.modsen.bookstorageservice.domain.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.domain.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.dto.BookDto;
import com.modsen.bookstorageservice.mapper.BookMapper;
import com.modsen.bookstorageservice.repository.BookRepository;
import com.modsen.bookstorageservice.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookServiceImpl implements BookService {


    BookRepository bookRepository;
    BookMapper bookMapper;
    RabbitServiceImpl rabbitService;

    @Override
    public BookDto getBookById(Long id) {
        log.info("Request for book by id: {}", id);

        BookDto bookDto = bookMapper.toDto(
                bookRepository.findById(id).orElseThrow(() ->
                        new ResourceNotFoundException("Book with id " + id + " not found"))
        );

        log.info("Book with id {} found: {}", id, bookDto);
        return bookDto;
    }

    @Override
    public BookDto getBookByIsbn(String isbn) {
        log.info("Request for book by isbn: {}", isbn);

        BookDto bookDto = bookMapper.toDto(
                bookRepository.findByIsbn(isbn).orElseThrow(() ->
                        new ResourceNotFoundException("Book with isbn " + isbn + " not found"))
        );

        log.info("Book with isbn {} found: {}", isbn, bookDto);
        return bookDto;
    }

    @Override
    public List<BookDto> getAllBooks() {
        log.info("Request for all books");

        List<BookDto> books = bookMapper.toDto(bookRepository.findAll());

        log.info("Received {} books", books.size());
        return books;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        log.info("Creating book with data: {}", bookDto);

        Book book = bookMapper.toEntity(bookDto);
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            log.warn("Attempt to create book with existing isbn: {}", book.getIsbn());
            throw new DuplicateResourceException("Book with isbn " + bookDto.getIsbn() + " already exists");
        }

        bookRepository.save(book);
        rabbitService.addCreateBook(book.getId());

        log.info("Book successfully created with id: {}", book.getId());
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        log.info("Updating book with data: {}", bookDto);

        Book book = bookMapper.toEntity(bookDto);
        bookRepository.findById(book.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Book with id " + bookDto.getId() + " not found"));

        bookRepository.save(book);

        log.info("Book with id {} successfully updated", bookDto.getId());
        return bookDto;
    }

    public void deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);

        Book book = bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book with id " + id + " not found"));

        rabbitService.addDeleteBook(book.getId());
        bookRepository.delete(book);

        log.info("Book with id {} successfully deleted", id);
    }


}
