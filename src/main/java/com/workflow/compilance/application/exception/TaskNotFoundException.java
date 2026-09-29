package com.workflow.compilance.application.exception;

public class TaskNotFoundException extends RuntimeException {
    private final String workActionId;

    public TaskNotFoundException(String workActionId) {
        super("Work action not found: " + workActionId);
        this.workActionId = workActionId;
    }

    public String getWorkActionId() {
        return workActionId;
    }
}
