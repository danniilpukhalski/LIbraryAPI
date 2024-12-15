package com.modsen.bookstorageservice.service.impl;

import com.modsen.bookstorageservice.domain.Book;
import com.modsen.bookstorageservice.domain.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.domain.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.repository.BookRepository;
import com.modsen.bookstorageservice.service.BookService;
import com.modsen.bookstorageservice.web.dto.BookDto;
import com.modsen.bookstorageservice.web.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

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
        book.setDeleted(false);
        bookRepository.save(book);
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

    public void softDeleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        book.setDeleted(true);
        bookRepository.save(book);
    }


}
