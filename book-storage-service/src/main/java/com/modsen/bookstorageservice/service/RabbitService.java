package com.modsen.bookstorageservice.service;

import org.springframework.stereotype.Service;

@Service
public interface RabbitService {

    public void addCreateBook(Long bookId);

    public void addDeleteBook(Long bookId);
}


