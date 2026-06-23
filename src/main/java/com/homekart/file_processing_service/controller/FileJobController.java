package com.homekart.file_processing_service.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.homekart.file_processing_service.dto.UploadResponse;
import com.homekart.file_processing_service.model.FileJob;
import com.homekart.file_processing_service.service.FileJobService;
import com.homekart.file_processing_service.service.ProcessingMetrics;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/file-processing")
@RequiredArgsConstructor
public class FileJobController {

    private final FileJobService fileJobService;
    private final ProcessingMetrics processingMetrics;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> getFile(@RequestParam("file") MultipartFile file)
            throws IOException {

        String jobId = fileJobService.uploadFile(file);

        return ResponseEntity.ok(
                new UploadResponse(jobId, "PENDING"));

    }

    @GetMapping("/{jobId}")
    public ResponseEntity<FileJob> getJob(@PathVariable String jobId) {

        return ResponseEntity.ok(
                fileJobService.getFileJob(jobId));
    }

    @GetMapping("/metrics")
    public int getMetrics() {

        return processingMetrics.getProcessedFiles();
    }

}
