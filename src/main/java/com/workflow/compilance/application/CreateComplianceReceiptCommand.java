package com.workflow.compilance.application;

import com.workflow.compilance.api.dto.request.CreateComplianceReceiptRequest;
import com.workflow.compilance.domain.DeliveryChannel;
import com.workflow.compilance.domain.ReceiptType;

import java.time.Instant;

public record CreateComplianceReceiptCommand(
        String workActionId,
        String customerId,
        ReceiptType receiptType,
        DeliveryChannel deliveryChannel,
        String recipient,
        Instant receivedAt,
        String referenceNumber,
        String notes
) {
    public static CreateComplianceReceiptCommand fromRequest(CreateComplianceReceiptRequest request) {
        return new CreateComplianceReceiptCommand(
                request.workActionId(),
                request.customerId(),
                request.receiptType(),
                request.deliveryChannel(),
                request.recipient(),
                request.receivedAt(),
                request.referenceNumber(),
                request.notes()
        );
    }
}
