package com.example.demo.controller;

import com.example.demo.service.PricingPlanService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("request")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class RequestController {

    private PricingPlanService pricingPlanService;

    @GetMapping()
    public ResponseEntity<String> rectangle() {
        return ResponseEntity.ok()
                .body("Request OK");
    }



}
