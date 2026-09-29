package com.workflow.compliance.application;

import com.workflow.compilance.application.ComplianceReceiptSearchCommand;
import com.workflow.compilance.application.ComplianceReceiptSearchResult;
import com.workflow.compilance.application.ComplianceReceiptSearchUseCase;
import com.workflow.compilance.application.exception.InvalidComplianceReceiptException;
import com.workflow.compilance.domain.ComplianceReceiptRepository;
import com.workflow.compilance.domain.ComplianceReceiptStatus;
import com.workflow.compilance.domain.DeliveryChannel;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ComplianceReceiptSearchUseCaseTest {
    private final ComplianceReceiptRepository repository = mock(ComplianceReceiptRepository.class);
    private final ComplianceReceiptSearchUseCase useCase = new ComplianceReceiptSearchUseCase(repository);

    @Test
    void shouldReturnReceiptsForMultipleCriteria() {
        ComplianceReceiptSearchCommand command = new ComplianceReceiptSearchCommand(
                null, "12345", null, DeliveryChannel.EMAIL, ComplianceReceiptStatus.CREATED, null,
                Instant.parse("2026-09-01T00:00:00Z"), Instant.parse("2026-09-08T23:59:59Z"));
        when(repository.search(command)).thenReturn(List.of());

        ComplianceReceiptSearchResult result = useCase.search(command);

        assertThat(result.disclosureReceipts()).isEmpty();
        verify(repository).search(command);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldRejectEmptySearchCriteria() {
        ComplianceReceiptSearchCommand command = new ComplianceReceiptSearchCommand(
                null, null, null, null, null, null, null, null);

        assertThatThrownBy(() -> useCase.search(command))
                .isInstanceOf(InvalidComplianceReceiptException.class)
                .hasMessage("At least one search criterion must be provided");
        verifyNoInteractions(repository);
    }

    @Test
    void shouldRejectInvalidDateRange() {
        ComplianceReceiptSearchCommand command = new ComplianceReceiptSearchCommand(
                "WA10001", null, null, null, null, null,
                Instant.parse("2026-09-09T00:00:00Z"), Instant.parse("2026-09-08T00:00:00Z"));

        assertThatThrownBy(() -> useCase.search(command))
                .isInstanceOf(InvalidComplianceReceiptException.class)
                .hasMessage("fromReceivedAt cannot be after toReceivedAt");
        verifyNoInteractions(repository);
    }
}
