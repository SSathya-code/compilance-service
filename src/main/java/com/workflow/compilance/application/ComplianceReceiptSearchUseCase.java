package com.workflow.compilance.application;

import com.workflow.compilance.application.exception.InvalidComplianceReceiptException;
import com.workflow.compilance.domain.ComplianceReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComplianceReceiptSearchUseCase {
    private final ComplianceReceiptRepository repository;

    public ComplianceReceiptSearchUseCase(ComplianceReceiptRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public ComplianceReceiptSearchResult search(ComplianceReceiptSearchCommand command) {
        validate(command);
        return new ComplianceReceiptSearchResult(List.copyOf(repository.search(command)));
    }

    private void validate(ComplianceReceiptSearchCommand command) {
        if (command == null) {
            throw new InvalidComplianceReceiptException("Request payload is required");
        }
        boolean noCriteria = isBlank(command.workActionId()) && isBlank(command.customerId())
                && command.receiptType() == null && command.deliveryChannel() == null && command.status() == null
                && isBlank(command.referenceNumber()) && command.fromReceivedAt() == null && command.toReceivedAt() == null;
        if (noCriteria) {
            throw new InvalidComplianceReceiptException("At least one search criterion must be provided");
        }
        if (command.fromReceivedAt() != null && command.toReceivedAt() != null
                && command.fromReceivedAt().isAfter(command.toReceivedAt())) {
            throw new InvalidComplianceReceiptException("fromReceivedAt cannot be after toReceivedAt");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
