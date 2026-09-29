package com.workflow.compilance.application.exception;

public class CompliancePersistenceException extends RuntimeException {
    private final String errorCode;

    public CompliancePersistenceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ERR-DISCLOSURE-DB-SAVE-FAILED";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
