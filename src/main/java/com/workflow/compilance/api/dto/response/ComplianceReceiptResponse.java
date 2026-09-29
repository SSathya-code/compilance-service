package com.workflow.compilance.api.dto.response;

import com.workflow.compilance.domain.ComplianceReceiptStatus;
import com.workflow.compilance.domain.DeliveryChannel;
import com.workflow.compilance.domain.ReceiptType;

import java.time.Instant;

public record ComplianceReceiptResponse(
        String disclosureReceiptId,
        String workActionId,
        String customerId,
        ReceiptType receiptType,
        DeliveryChannel deliveryChannel,
        String recipient,
        Instant receivedAt,
        String referenceNumber,
        ComplianceReceiptStatus status
) {
}
