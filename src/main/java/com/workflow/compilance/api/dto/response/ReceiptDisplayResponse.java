package com.workflow.compilance.api.dto.response;

import java.time.Instant;

public record ReceiptDisplayResponse(
        String eventId,
        String receiptId,
        String topic,
        Instant publishedAt) {
}
