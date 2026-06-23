package com.homekart.file_processing_service.service;

import org.springframework.stereotype.Component;

@Component
public class ProcessingMetrics {

    private int processedFiles = 0;

    public void increment() {

        try {
            Thread.sleep(1);
        } catch (Exception e) {

        }
        processedFiles++;
        System.out.println("processed files count: " + processedFiles);
    }

    public int getProcessedFiles() {
        return processedFiles;
    }

}
