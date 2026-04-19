package com.example.leakyservice.controller;

import com.example.leakyservice.model.TransactionDocument;
import com.example.leakyservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDocument> createTransaction(
            @RequestParam String userId,
            @RequestParam BigDecimal amount) {

        log.info("Creating transaction for user: {}, amount: {}", userId, amount);
        TransactionDocument doc = transactionService.createTransaction(userId, amount);
        return ResponseEntity.ok(doc);
    }

    @GetMapping("/cache/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        return ResponseEntity.ok(transactionService.getCacheStats());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "OK", "message", "Leaky service is running"));
    }
}