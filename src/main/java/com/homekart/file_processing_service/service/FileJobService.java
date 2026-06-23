package com.homekart.file_processing_service.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.homekart.file_processing_service.model.FileJob;
import com.homekart.file_processing_service.repository.FileJobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileJobService {

    private final S3Service s3Service;
    private final FileJobRepository fileJobRepository;
    private final FileProcessingQueue queue;
    private final ProcessingService processingService;

    public String uploadFile(MultipartFile file) throws IOException {

        String jobId = UUID.randomUUID().toString();

        String s3Key = s3Service.uploadFile(file);

        FileJob fileJob = FileJob.builder()
                .jobId(jobId)
                .fileName(file.getOriginalFilename())
                .s3Key(s3Key)
                .status("PENDING")
                .createdTime(LocalDateTime.now())
                .build();

        fileJobRepository.save(fileJob);

        try {
            queue.addJob(jobId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to add job to queue", e);
        }

        return "FileJob saved to DynamoDB with Job ID: " + jobId;

    }

    public FileJob getFileJob(String jobId) {

        return fileJobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job Not Found"));
    }

    public void processFile(String jobId) {

        System.out.println("Current thread: " + Thread.currentThread().getName());

        try {
            FileJob fileJob = fileJobRepository.findById(jobId).orElseThrow();
            fileJob.setStatus("PROCESSING");
            fileJobRepository.update(fileJob);

            System.out.println("Processing started for job: " + jobId);

            CompletableFuture<String> metadataTask = CompletableFuture.supplyAsync(
                    () -> processingService.extractMetadata(jobId));

            CompletableFuture<String> virusTask = CompletableFuture.supplyAsync(
                    () -> processingService.scanFile(jobId));

            CompletableFuture<String> thumbnailTask = CompletableFuture.supplyAsync(
                    () -> processingService.generateThumbnail(jobId));

            CompletableFuture.allOf(
                    metadataTask,
                    virusTask,
                    thumbnailTask).join();

            System.out.println(metadataTask.join());
            System.out.println(virusTask.join());
            System.out.println(thumbnailTask.join());

            fileJob.setStatus("COMPLETED");
            fileJob.setProcessedTime(LocalDateTime.now());
            fileJobRepository.update(fileJob);

            System.out.println("Processing completed for Job: " + jobId);
        } catch (Exception e) {

            FileJob fileJob = fileJobRepository.findById(jobId).orElseThrow();
            fileJob.setStatus("FAILED");
            fileJobRepository.update(fileJob);
            e.printStackTrace();
        }
    }

}
