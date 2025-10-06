package com.example.demo.controller;

import com.example.demo.service.JobQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobQueueService jobQueueService;

    @GetMapping("/job/{id}")
    public ResponseEntity<?> getJobStatus(@PathVariable String id) {
        String status = jobQueueService.getStatus(id);
        return ResponseEntity.ok(Map.of(
                "jobId", id,
                "status", status
        ));
    }
}
