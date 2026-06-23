package com.homekart.file_processing_service.service;

import org.springframework.stereotype.Service;

@Service
public class ProcessingService {

    public String extractMetadata(String jobId) {

        try {
            System.out.println("Metadata started for " + jobId);
            Thread.sleep(10000);
            System.out.println("Metadata completed for " + jobId);
            return "Metadata done";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String scanFile(String jobId) {
        try {
            System.out.println("Virus scan started for " + jobId);
            Thread.sleep(15000);
            System.out.println("Virus scan completed for " + jobId);
            return "Virus scan done";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generateThumbnail(String jobId) {
        try {
            System.out.println("Thumbnail started for " + jobId);
            Thread.sleep(12000);
            System.out.println("Thumbnail completed for " + jobId);
            return "Thumbnail done";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
