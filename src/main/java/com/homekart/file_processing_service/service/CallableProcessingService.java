package com.homekart.file_processing_service.service;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Service;

@Service
public class CallableProcessingService {

    public Callable<String> metaDataTask(String jobId) {

        return () -> {
            System.out.println("Metadata extraction started for " + jobId);
            Thread.sleep(3000);
            System.out.println("Metadata extraction completed for " + jobId);

            return "Metadata extracted";
        };
    }

}
