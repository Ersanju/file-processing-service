package com.homekart.file_processing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class FileProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileProcessingServiceApplication.class, args);
		System.out.println("File processing service running... ");
	}

}
