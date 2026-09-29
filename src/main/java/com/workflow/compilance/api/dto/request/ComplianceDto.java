package com.workflow.compilance.api.dto.request;

import java.time.Instant;

public record ComplianceDto(Long id, String referenceNumber, String customerId,
                            String description, String status, Instant createdAt) {
}
