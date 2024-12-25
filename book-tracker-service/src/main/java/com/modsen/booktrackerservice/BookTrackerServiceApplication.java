package com.modsen.booktrackerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
@EnableAspectJAutoProxy
public class BookTrackerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookTrackerServiceApplication.class, args);
    }

}
