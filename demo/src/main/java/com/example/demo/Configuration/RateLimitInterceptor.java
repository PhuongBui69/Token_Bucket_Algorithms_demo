package com.example.demo.Configuration;

import com.example.demo.enums.PricingPlan;
import com.example.demo.service.JobQueueService;
import com.example.demo.service.PricingPlanService;
import com.example.demo.service.ThrottlingService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class    RateLimitInterceptor implements HandlerInterceptor {

    PricingPlanService pricingPlanService;
    ThrottlingService throttlingService;
    JobQueueService jobQueueService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        String apiKey = request.getHeader("X-api-key");
        if (apiKey == null || apiKey.isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: X-api-key");
            return false;
        }

        Bucket tokenBucket = pricingPlanService.resolveBucket(apiKey);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true; // xử lý bình thường
        } else {
            long waitSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;
            String jobId = jobQueueService.createJob();

            throttlingService.submitRequest(jobId, () -> {
                System.out.println("Processing throttled job for key: " + apiKey);
            }, tokenBucket);

            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setContentType("application/json");
            response.getWriter().write(Map.of(
                    "status", "QUEUED",
                    "jobId", jobId,
                    "retryAfterSeconds", String.valueOf(waitSeconds),
                    "message", "Your request is queued for later processing."
            ).toString());
            return false;
        }
    }
}
