package com.example.demo.service;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThrottlingService {

    private final JobQueueService jobQueueService;

    // Hàng đợi lưu jobId + công việc
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(1000);
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void submitRequest(String jobId, Runnable task, Bucket bucket) {
        // Xếp request vào hàng đợi
        if (!queue.offer(() -> processWithBucket(jobId, task, bucket))) {
            log.warn("Queue full! Job {} bị từ chối", jobId);
            jobQueueService.markDone(jobId);
        } else {
            log.info("Job {} added to queue", jobId);
            executor.submit(() -> {
                try {
                    queue.take().run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    private void processWithBucket(String jobId, Runnable task, Bucket bucket) {
        try {
            jobQueueService.markProcessing(jobId);
            bucket.asBlocking().consume(1); // chờ đến khi có token
            task.run();
            jobQueueService.markDone(jobId);
            log.info("Job {} processed successfully", jobId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Job {} interrupted", jobId);
        }
    }
}
