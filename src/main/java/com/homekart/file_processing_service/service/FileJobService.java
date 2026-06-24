package com.homekart.file_processing_service.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
// import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
    // private final ProcessingService processingService;
    private final ProcessingMetrics processingMetrics;
    // private final ReportService reportService;
    // private final ChunkProcessingService chunkProcessingService;
    // private final CyclicBarrierService cyclicBarrierService;
    // private final SemaphoreService semaphoreService;
    private final AsyncProcessingService asyncProcessingService;
    private final CallableProcessingService callableProcessingService;
    private final ExecutorService executorService;

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

        try {

            FileJob fileJob = fileJobRepository.findById(jobId)
                    .orElseThrow();

            fileJob.setStatus("PROCESSING");
            fileJobRepository.update(fileJob);

            // ==================================================
            // Phase 4 - Manual Thread
            // ==================================================

            // Thread.sleep(30000);

            // ==================================================
            // Phase 7 - CompletableFuture
            // Metadata + Virus Scan + Thumbnail
            // ==================================================

            // CompletableFuture<String> metadataTask =
            // CompletableFuture.supplyAsync(
            // () -> processingService.extractMetadata(jobId));

            // CompletableFuture<String> virusTask =
            // CompletableFuture.supplyAsync(
            // () -> processingService.scanFile(jobId));

            // CompletableFuture<String> thumbnailTask =
            // CompletableFuture.supplyAsync(
            // () -> processingService.generateThumbnail(jobId));

            // CompletableFuture.allOf(
            // metadataTask,
            // virusTask,
            // thumbnailTask)
            // .join();

            // ==================================================
            // Phase 9 - synchronized
            // ==================================================

            // reportService.generateReport(jobId);

            // ==================================================
            // Phase 10 - ReentrantLock + tryLock
            // ==================================================

            // reportService.generateReport(jobId);

            // ==================================================
            // Phase 12 - CountDownLatch
            // ==================================================

            // chunkProcessingService.processChunks(jobId);

            // ==================================================
            // Phase 13 - CyclicBarrier
            // ==================================================

            // cyclicBarrierService.processChunksWithBarrier(jobId);

            // ==================================================
            // Phase 14 - Semaphore
            // ==================================================

            // semaphoreService.accessS3(jobId);

            // ==================================================
            // Phase 15 - Spring Async
            // ==================================================

            asyncProcessingService.sendNotification(jobId);

            // ==================================================
            // Phase 17 - Callable & Future
            // ==================================================

            Future<String> metadataFuture = executorService.submit(
                    callableProcessingService.metaDataTask(jobId));

            String metadataResult = metadataFuture.get();

            System.out.println(
                    "Future result : " + metadataResult);

            fileJob.setStatus("COMPLETED");
            fileJob.setProcessedTime(LocalDateTime.now());

            fileJobRepository.update(fileJob);

            // ==================================================
            // Phase 8 - AtomicInteger
            // ==================================================

            processingMetrics.increment();

        } catch (Exception e) {

            FileJob fileJob = fileJobRepository.findById(jobId)
                    .orElseThrow();

            fileJob.setStatus("FAILED");
            fileJobRepository.update(fileJob);

            e.printStackTrace();
        }
    }
}
