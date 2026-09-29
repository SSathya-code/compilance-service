package com.workflow.compilance.application;

import org.springframework.stereotype.Component;

@Component
public class DefaultTaskGateway implements TaskGateway {
    @Override
    public boolean exists(String workActionId) {
        return workActionId != null && !workActionId.isBlank();
    }
}
