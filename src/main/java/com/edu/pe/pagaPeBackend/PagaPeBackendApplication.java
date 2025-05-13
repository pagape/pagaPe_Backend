package com.edu.pe.pagaPeBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PagaPeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagaPeBackendApplication.class, args);
	}

}
