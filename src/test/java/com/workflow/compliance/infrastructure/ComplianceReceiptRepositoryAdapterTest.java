package com.workflow.compliance.infrastructure;

import com.workflow.compilance.application.ComplianceReceiptSearchCommand;
import com.workflow.compilance.infrastructure.ComplianceReceiptRepositoryAdapter;
import com.workflow.compilance.infrastructure.SpringDataComplianceReceiptRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ComplianceReceiptRepositoryAdapterTest {

    @Test
    void shouldSearchWhenOnlyOneOptionalCriterionIsProvided() {
        SpringDataComplianceReceiptRepository repository = mock(SpringDataComplianceReceiptRepository.class);
        when(repository.findAll(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(List.of());
        ComplianceReceiptRepositoryAdapter adapter = new ComplianceReceiptRepositoryAdapter(repository);
        ComplianceReceiptSearchCommand command = new ComplianceReceiptSearchCommand(
                null, "CUST-101", null, null, null, null, null, null);

        assertDoesNotThrow(() -> adapter.search(command));
    }
}
