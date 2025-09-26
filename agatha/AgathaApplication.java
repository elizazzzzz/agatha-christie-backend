package com.agatha.agatha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.agatha.agatha.entity")
public class AgathaApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgathaApplication.class, args);
    }

}
