package com.example.leakyservice.service;

import com.example.leakyservice.model.TransactionDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class TransactionService {

    // ⚠️⚠️⚠️ ЭТО УТЕЧКА ПАМЯТИ! ⚠️⚠️⚠️
    private static final ConcurrentHashMap<String, TransactionDocument> CACHE = new ConcurrentHashMap<>();

    private final AtomicLong counter = new AtomicLong(0);

    public TransactionDocument createTransaction(String userId, BigDecimal amount) {

        TransactionDocument doc = TransactionDocument.builder()
                .userId(userId)
                .amount(amount)
                .currency("RUB")
                .description("Transaction #" + counter.incrementAndGet())
                .tags(List.of("tag1", "tag2", "tag3"))
                .metadata(Map.of(
                        "source", "web",
                        "device", "mobile",
                        "location", "Moscow",
                        "sessionId", "sess_" + System.currentTimeMillis()
                ))
                .auditLogs(List.of(
                        TransactionDocument.AuditLog.builder()
                                .action("CREATED")
                                .user("system")
                                .timestamp(LocalDateTime.now())
                                .details("Transaction created")
                                .build(),
                        TransactionDocument.AuditLog.builder()
                                .action("VALIDATED")
                                .user("system")
                                .timestamp(LocalDateTime.now())
                                .details("Validation passed")
                                .build()
                ))
                .approvalHistory(List.of(
                        TransactionDocument.ApprovalStep.builder()
                                .approver("auto-approver")
                                .status("APPROVED")
                                .timestamp(LocalDateTime.now())
                                .comment("Auto-approved")
                                .build()
                ))
                .build();

        // ⚠️ УТЕЧКА: кладём в кэш и НИКОГДА не удаляем!
        CACHE.put(doc.getId(), doc);

        log.info("Created transaction: {}, cache size: {}", doc.getId(), CACHE.size());

        if (CACHE.size() % 100 == 0) {
            log.warn("⚠️ Cache size is now: {} objects! Potential memory leak!", CACHE.size());
        }

        return doc;
    }

    public Map<String, Object> getCacheStats() {
        return Map.of(
                "size", CACHE.size(),
                "estimatedMemoryMB", CACHE.size() * 50L / 1024
        );
    }
}