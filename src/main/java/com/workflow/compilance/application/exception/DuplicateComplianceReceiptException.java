package com.workflow.compilance.application.exception;

import com.workflow.compilance.domain.ReceiptType;

public class DuplicateComplianceReceiptException extends RuntimeException {
    private final String workActionId;
    private final ReceiptType receiptType;
    private final String referenceNumber;

    public DuplicateComplianceReceiptException(String workActionId, ReceiptType receiptType, String referenceNumber) {
        super("Duplicate disclosure receipt for workActionId=" + workActionId + ", receiptType=" + receiptType + ", referenceNumber=" + referenceNumber);
        this.workActionId = workActionId;
        this.receiptType = receiptType;
        this.referenceNumber = referenceNumber;
    }

    public String getWorkActionId() {
        return workActionId;
    }

    public ReceiptType getReceiptType() {
        return receiptType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }
}
