package com.workflow.compilance.api.dto.response;

import com.workflow.compilance.domain.ComplianceReceiptStatus;

public record CreateComplianceReceiptResponse(
        String disclosureReceiptId,
        String workActionId,
        String customerId,
        ComplianceReceiptStatus status
) {
}
