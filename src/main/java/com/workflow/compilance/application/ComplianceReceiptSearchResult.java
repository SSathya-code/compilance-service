package com.workflow.compilance.application;

import com.workflow.compilance.domain.ComplianceReceipt;

import java.util.List;

public record ComplianceReceiptSearchResult(List<ComplianceReceipt> disclosureReceipts) {
}
