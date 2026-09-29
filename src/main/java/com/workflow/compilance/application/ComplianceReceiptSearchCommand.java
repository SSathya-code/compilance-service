package com.workflow.compilance.application;

import com.workflow.compilance.api.dto.request.ComplianceReceiptSearchRequest;
import com.workflow.compilance.domain.ComplianceReceiptStatus;
import com.workflow.compilance.domain.DeliveryChannel;
import com.workflow.compilance.domain.ReceiptType;

import java.time.Instant;

public record ComplianceReceiptSearchCommand(
        String workActionId, String customerId, ReceiptType receiptType, DeliveryChannel deliveryChannel,
        ComplianceReceiptStatus status, String referenceNumber, Instant fromReceivedAt, Instant toReceivedAt
) {
    public static ComplianceReceiptSearchCommand fromRequest(ComplianceReceiptSearchRequest request) {
        return new ComplianceReceiptSearchCommand(request.workActionId(), request.customerId(), request.receiptType(),
                request.deliveryChannel(), request.status(), request.referenceNumber(), request.fromReceivedAt(), request.toReceivedAt());
    }
}
