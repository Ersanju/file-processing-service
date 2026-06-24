package com.homekart.file_processing_service.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final ProcessingMetrics processingMetrics;

    @Scheduled(fixedRate = 10000)
    public void printMetrics() {

        System.out.println("=======SCHEDULER========");
        System.out.println("Proccessed file count: " + processingMetrics.getCount());
        System.out.println("Current Time : " + java.time.LocalDateTime.now());
        System.out.println("===========================================");
    }

}
