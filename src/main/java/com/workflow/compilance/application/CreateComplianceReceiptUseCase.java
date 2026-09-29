package com.workflow.compilance.application;

import com.workflow.compilance.application.exception.DuplicateComplianceReceiptException;
import com.workflow.compilance.application.exception.InvalidComplianceReceiptException;
import com.workflow.compilance.application.exception.TaskNotFoundException;
import com.workflow.compilance.domain.ComplianceReceipt;
import com.workflow.compilance.domain.ComplianceReceiptRepository;
import com.workflow.compilance.domain.DeliveryChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CreateComplianceReceiptUseCase {
    private final ComplianceReceiptRepository repository;
    private final TaskGateway workActionGateway;

    public CreateComplianceReceiptUseCase(ComplianceReceiptRepository repository, TaskGateway workActionGateway) {
        this.repository = repository;
        this.workActionGateway = workActionGateway;
    }

    @Transactional
    public CreateComplianceReceiptResult create(CreateComplianceReceiptCommand command) {
        validateMandatoryFields(command);

        if (!workActionGateway.exists(command.workActionId())) {
            throw new TaskNotFoundException(command.workActionId());
        }

        if (repository.existsByWorkActionIdAndReceiptTypeAndReferenceNumber(
                command.workActionId(),
                command.receiptType(),
                command.referenceNumber())) {
            throw new DuplicateComplianceReceiptException(command.workActionId(), command.receiptType(), command.referenceNumber());
        }

        if (command.deliveryChannel() == DeliveryChannel.EMAIL && !isValidEmail(command.recipient())) {
            throw new InvalidComplianceReceiptException("EMAIL delivery requires valid email recipient");
        }

        if (command.receivedAt() == null || command.receivedAt().isAfter(Instant.now())) {
            throw new InvalidComplianceReceiptException("receivedAt cannot be future date");
        }

        ComplianceReceipt saved = repository.save(ComplianceReceipt.create(
                command.workActionId(),
                command.customerId(),
                command.receiptType(),
                command.deliveryChannel(),
                command.recipient(),
                command.receivedAt(),
                command.referenceNumber(),
                command.notes()
        ));

        return new CreateComplianceReceiptResult(
                saved.disclosureReceiptId(),
                saved.workActionId(),
                saved.customerId(),
                saved.status()
        );
    }

    private void validateMandatoryFields(CreateComplianceReceiptCommand command) {
        if (command == null) {
            throw new InvalidComplianceReceiptException("Request payload is required");
        }
        if (command.workActionId() == null || command.workActionId().isBlank()) {
            throw new InvalidComplianceReceiptException("workActionId is mandatory");
        }
        if (command.customerId() == null || command.customerId().isBlank()) {
            throw new InvalidComplianceReceiptException("customerId is mandatory");
        }
        if (command.receiptType() == null) {
            throw new InvalidComplianceReceiptException("receiptType is mandatory");
        }
        if (command.deliveryChannel() == null) {
            throw new InvalidComplianceReceiptException("deliveryChannel is mandatory");
        }
        if (command.recipient() == null || command.recipient().isBlank()) {
            throw new InvalidComplianceReceiptException("recipient is mandatory");
        }
        if (command.receivedAt() == null) {
            throw new InvalidComplianceReceiptException("receivedAt is mandatory");
        }
        if (command.referenceNumber() == null || command.referenceNumber().isBlank()) {
            throw new InvalidComplianceReceiptException("referenceNumber is mandatory");
        }
    }

    private boolean isValidEmail(String recipient) {
        return recipient != null && recipient.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
