package com.workflow.compilance.domain;

import java.time.Instant;
import java.util.UUID;

public record ComplianceReceipt(
        String disclosureReceiptId,
        String workActionId,
        String customerId,
        ReceiptType receiptType,
        DeliveryChannel deliveryChannel,
        String recipient,
        Instant receivedAt,
        String referenceNumber,
        String notes,
        ComplianceReceiptStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static ComplianceReceipt create(
            String workActionId,
            String customerId,
            ReceiptType receiptType,
            DeliveryChannel deliveryChannel,
            String recipient,
            Instant receivedAt,
            String referenceNumber,
            String notes
    ) {
        Instant now = Instant.now();
        String id = "DR" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return new ComplianceReceipt(
                id,
                workActionId,
                customerId,
                receiptType,
                deliveryChannel,
                recipient,
                receivedAt,
                referenceNumber,
                notes,
                ComplianceReceiptStatus.CREATED,
                now,
                now
        );
    }
}
