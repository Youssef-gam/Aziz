package com.parc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.parc") // ou simplement @SpringBootApplication
public class GestionParcApplication {
    public static void main(String[] args) {
        SpringApplication.run(GestionParcApplication.class, args);
    }
}