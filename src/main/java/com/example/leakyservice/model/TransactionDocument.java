package com.example.leakyservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class TransactionDocument {

    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String userId;
    private BigDecimal amount;
    private String currency;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String description;
    private List<String> tags;
    private Map<String, Object> metadata;

    // СИМУЛЯЦИЯ "ЖИРНОГО" ОБЪЕКТА
    @Builder.Default
    private byte[] attachment = new byte[10 * 1024]; // 10 KB

    private List<AuditLog> auditLogs;
    private List<ApprovalStep> approvalHistory;

    @Data
    @Builder
    public static class AuditLog {
        private String action;
        private String user;
        private LocalDateTime timestamp;
        private String details;
    }

    @Data
    @Builder
    public static class ApprovalStep {
        private String approver;
        private String status;
        private LocalDateTime timestamp;
        private String comment;
    }
}
