package com.modsen.bookstorageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class BookStorageServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(BookStorageServiceApplication.class, args);
    }


}
