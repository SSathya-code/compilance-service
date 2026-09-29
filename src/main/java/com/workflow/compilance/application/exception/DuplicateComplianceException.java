package com.workflow.compilance.application.exception;

public class DuplicateComplianceException extends RuntimeException {
    private final String errorCode = "ERR-DISCLOSURE-DUPLICATE";

    public DuplicateComplianceException(String referenceNumber) {
        super("Duplicate disclosure reference: " + referenceNumber);
    }

    public String getErrorCode() {
        return errorCode;
    }
}
