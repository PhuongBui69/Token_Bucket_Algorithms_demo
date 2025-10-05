package com.example.demo.controller;

import com.example.demo.dto.request.RectangleRequest;
import com.example.demo.dto.request.TriangleRequest;
import com.example.demo.dto.response.RectangleResponse;
import com.example.demo.dto.response.TriangleResponse;
import com.example.demo.service.PricingPlanService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("area")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AreaCalculationController {

    private PricingPlanService pricingPlanService;

    @PostMapping("/rectangle")
    public ResponseEntity<RectangleResponse> rectangle(@RequestBody RectangleRequest request) {
        return ResponseEntity.ok()
                .body(RectangleResponse.builder()
                        .name("Rectangle")
                        .area(request.getHeight() * request.getWidth())
                        .build());
    }

    @PostMapping(value = "/triangle")
    public ResponseEntity<TriangleResponse> triangle(@RequestBody TriangleRequest request) {
        return ResponseEntity.ok()
                .body(TriangleResponse.builder()
                        .name("Triangle")
                        .area(request.getHeight() * request.getWidth())
                        .build());
    }


}
