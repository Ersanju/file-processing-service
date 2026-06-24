package com.homekart.file_processing_service.service;

import java.util.concurrent.CyclicBarrier;

import org.springframework.stereotype.Service;

@Service
public class CyclicBarrierService {

    public void processChunksWithBarrier(String jobId) {

        CyclicBarrier barrier = new CyclicBarrier(5,
                () -> System.out.println("=== ALL CHUNKS COMPLETED CURRENT STAGE ==="));

        for (int i = 1; i <= 5; i++) {
            int chunkNo = i;
            new Thread(() -> {
                try {
                    System.out.println("Chunk " + chunkNo + " Stage 1 started");
                    Thread.sleep(2000);
                    System.out.println("Chunk " + chunkNo + " Stage 1 completed");
                    barrier.await();

                    System.out.println("Chunk " + chunkNo + " Stage 2 started");
                    Thread.sleep(2000);
                    System.out.println("Chunk " + chunkNo + " Stage 2 completed");
                    barrier.await();

                    System.out.println("Chunk " + chunkNo + " Stage 3 started");
                    Thread.sleep(2000);
                    System.out.println("Chunk " + chunkNo + " Stage 3 completed");
                    barrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
