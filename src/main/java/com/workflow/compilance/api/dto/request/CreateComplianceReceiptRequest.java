package com.workflow.compilance.api.dto.request;

import com.workflow.compilance.domain.DeliveryChannel;
import com.workflow.compilance.domain.ReceiptType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CreateComplianceReceiptRequest(
        @NotBlank(message = "workActionId is mandatory") String workActionId,
        @NotBlank(message = "customerId is mandatory") String customerId,
        @NotNull(message = "receiptType is mandatory") ReceiptType receiptType,
        @NotNull(message = "deliveryChannel is mandatory") DeliveryChannel deliveryChannel,
        @NotBlank(message = "recipient is mandatory") String recipient,
        @NotNull(message = "receivedAt is mandatory") Instant receivedAt,
        @NotBlank(message = "referenceNumber is mandatory") String referenceNumber,
        String notes
) {
    @AssertTrue(message = "EMAIL delivery requires valid email recipient")
    public boolean isRecipientValidForDeliveryChannel() {
        if (deliveryChannel == null || recipient == null || recipient.isBlank()) {
            return true;
        }

        if (deliveryChannel == DeliveryChannel.EMAIL) {
            return recipient.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        }

        return true;
    }

    @AssertTrue(message = "receivedAt cannot be future date")
    public boolean isReceivedAtNotInFuture() {
        return receivedAt != null && !receivedAt.isAfter(Instant.now());
    }
}
