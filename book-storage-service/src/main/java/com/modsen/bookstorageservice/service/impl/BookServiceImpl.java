package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.domain.Book;
import com.modsen.bookstorageservice.domain.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.domain.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.repository.BookRepository;
import com.modsen.bookstorageservice.service.BookService;
import com.modsen.bookstorageservice.web.dto.BookDto;
import com.modsen.bookstorageservice.web.mapper.BookMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookServiceImpl implements BookService {


    BookRepository bookRepository;
    BookMapper bookMapper;
    RabbitServiceImpl rabbitService;

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Book not found")));
    }

    @Override
    public BookDto getBookByIsbn(String isbn) {
        return bookMapper.toDto(bookRepository.findByIsbn(isbn).orElseThrow(() ->
                new ResourceNotFoundException("Book not found")));

    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookMapper.toDto(bookRepository.findAll());
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Book already exists");
        }
        bookRepository.save(book);
        rabbitService.addCreateBook(bookDto.getId());
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        bookRepository.findById(book.getId()).orElseThrow(() ->
                new ResourceNotFoundException("Book not found"));

        bookRepository.save(book);
        return bookDto;
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        rabbitService.addDeleteBook(book.getId());
        bookRepository.delete(book);
    }


}
