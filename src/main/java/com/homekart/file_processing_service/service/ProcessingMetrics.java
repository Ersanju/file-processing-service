package com.homekart.file_processing_service.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

@Component
public class ProcessingMetrics {

    private AtomicInteger processedFiles = new AtomicInteger(0);

    public void increment() {

        int count = processedFiles.incrementAndGet();

        System.out.println("processed files count: " + count);
    }

    public int getProcessedFiles() {

        return processedFiles.get();
    }

    public int getCount() {
        return processedFiles.get();
    }

}
