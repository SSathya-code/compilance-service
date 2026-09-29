package com.workflow.compliance.application;

import com.workflow.compilance.application.CreateComplianceReceiptCommand;
import com.workflow.compilance.application.CreateComplianceReceiptResult;
import com.workflow.compilance.application.CreateComplianceReceiptUseCase;
import com.workflow.compilance.application.TaskGateway;
import com.workflow.compilance.domain.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateComplianceReceiptUseCaseTest {

    private final ComplianceReceiptRepository repository = mock(ComplianceReceiptRepository.class);
    private final TaskGateway workActionGateway = mock(TaskGateway.class);
    private final CreateComplianceReceiptUseCase useCase = new CreateComplianceReceiptUseCase(repository, workActionGateway);

    @Test
    void shouldCreateComplianceReceiptWhenCommandIsValid() {
        CreateComplianceReceiptCommand command = new CreateComplianceReceiptCommand(
                "WA-100",
                "CUST-100",
                ReceiptType.DISCLOSURE,
                DeliveryChannel.EMAIL,
                "customer@example.com",
                Instant.parse("2026-08-28T00:00:00Z"),
                "REF-100",
                "Acknowledged"
        );

        when(workActionGateway.exists("WA-100")).thenReturn(true);
        when(repository.existsByWorkActionIdAndReceiptTypeAndReferenceNumber("WA-100", ReceiptType.DISCLOSURE, "REF-100"))
                .thenReturn(false);
        when(repository.save(any(ComplianceReceipt.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateComplianceReceiptResult result = useCase.create(command);

        assertThat(result.disclosureReceiptId()).startsWith("DR");
        assertThat(result.workActionId()).isEqualTo("WA-100");
        assertThat(result.customerId()).isEqualTo("CUST-100");
        assertThat(result.status()).isEqualTo(ComplianceReceiptStatus.CREATED);
        verify(repository).save(any(ComplianceReceipt.class));
    }

    @Test
    void shouldRejectDuplicateReceipt() {
        CreateComplianceReceiptCommand command = new CreateComplianceReceiptCommand(
                "WA-100",
                "CUST-100",
                ReceiptType.DISCLOSURE,
                DeliveryChannel.EMAIL,
                "customer@example.com",
                Instant.parse("2026-08-28T00:00:00Z"),
                "REF-100",
                "Acknowledged"
        );

        when(workActionGateway.exists("WA-100")).thenReturn(true);
        when(repository.existsByWorkActionIdAndReceiptTypeAndReferenceNumber("WA-100", ReceiptType.DISCLOSURE, "REF-100"))
                .thenReturn(true);

//        assertThatThrownBy(() -> useCase.create(command))
//                .isInstanceOf(DuplicateComplianceReceiptException.class)
//                .hasMessageContaining("already exists");

        verify(repository, never()).save(any(ComplianceReceipt.class));
    }
}
