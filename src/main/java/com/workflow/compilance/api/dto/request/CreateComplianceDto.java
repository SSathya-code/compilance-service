package com.workflow.compilance.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateComplianceDto(
        @NotBlank(message = "{disclosure.validation.mandatory-field}")
        @Size(max = 50, message = "{disclosure.validation.reference-number-size}")
        String referenceNumber,
        @NotBlank(message = "{disclosure.validation.mandatory-field}") String customerId,
        @NotBlank(message = "{disclosure.validation.mandatory-field}") String description) {
}
