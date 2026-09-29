package com.workflow.compilance.application;

import com.workflow.compilance.domain.ComplianceReceiptStatus;

public record CreateComplianceReceiptResult(
        String disclosureReceiptId,
        String workActionId,
        String customerId,
        ComplianceReceiptStatus status
) {
}
