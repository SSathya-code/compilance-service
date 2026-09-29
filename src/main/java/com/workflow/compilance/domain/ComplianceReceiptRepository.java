package com.workflow.compilance.domain;

import com.workflow.compilance.application.ComplianceReceiptSearchCommand;

public interface ComplianceReceiptRepository {
    ComplianceReceipt save(ComplianceReceipt disclosureReceipt);

    boolean existsByWorkActionIdAndReceiptTypeAndReferenceNumber(String workActionId, ReceiptType receiptType, String referenceNumber);

    java.util.List<ComplianceReceipt> search(ComplianceReceiptSearchCommand command);
}
