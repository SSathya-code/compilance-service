package com.workflow.compilance.infrastructure;

import com.workflow.compilance.domain.ReceiptType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataComplianceReceiptRepository extends JpaRepository<ComplianceReceiptJpaEntity, Long>, JpaSpecificationExecutor<ComplianceReceiptJpaEntity> {
    boolean existsByWorkActionIdAndReceiptTypeAndReferenceNumber(String workActionId, ReceiptType receiptType, String referenceNumber);
}
