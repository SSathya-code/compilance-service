package com.workflow.compilance.api.dto.request;

import com.workflow.compilance.domain.ComplianceReceiptStatus;
import com.workflow.compilance.domain.DeliveryChannel;
import com.workflow.compilance.domain.ReceiptType;
import jakarta.validation.constraints.AssertTrue;

import java.time.Instant;

public record ComplianceReceiptSearchRequest(
        String workActionId,
        String customerId,
        ReceiptType receiptType,
        DeliveryChannel deliveryChannel,
        ComplianceReceiptStatus status,
        String referenceNumber,
        Instant fromReceivedAt,
        Instant toReceivedAt
) {
    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    @AssertTrue(message = "At least one search criterion must be provided")
    public boolean isSearchCriteriaProvided() {
        return hasText(workActionId) || hasText(customerId) || receiptType != null || deliveryChannel != null
                || status != null || hasText(referenceNumber) || fromReceivedAt != null || toReceivedAt != null;
    }

    @AssertTrue(message = "fromReceivedAt cannot be after toReceivedAt")
    public boolean isReceivedAtRangeValid() {
        return fromReceivedAt == null || toReceivedAt == null || !fromReceivedAt.isAfter(toReceivedAt);
    }
}
