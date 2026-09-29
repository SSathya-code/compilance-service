package com.workflow.compilance.domain;

import java.time.Instant;

public record Compliance(Long id, String referenceNumber, String customerId,
                         String description, String status, Instant createdAt) {
}
