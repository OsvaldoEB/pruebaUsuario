package com.example.prueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.prueba.usuario.repository")
@EntityScan(basePackages = "com.example.prueba.usuario.model")
public class PruebaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PruebaApplication.class, args);
    }

}
