package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JobQueueService {

    private final Map<String, String> jobStatus = new ConcurrentHashMap<>();

    public String createJob() {
        String jobId = UUID.randomUUID().toString();
        jobStatus.put(jobId, "QUEUED");
        return jobId;
    }

    public void markProcessing(String jobId) {
        jobStatus.put(jobId, "PROCESSING");
    }

    public void markDone(String jobId) {
        jobStatus.put(jobId, "DONE");
    }

    public String getStatus(String jobId) {
        return jobStatus.getOrDefault(jobId, "NOT_FOUND");
    }
}
