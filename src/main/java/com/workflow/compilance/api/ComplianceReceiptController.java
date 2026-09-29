package com.workflow.compilance.api;

import com.workflow.compilance.api.dto.request.ComplianceReceiptSearchRequest;
import com.workflow.compilance.api.dto.request.CreateComplianceReceiptRequest;
import com.workflow.compilance.api.dto.response.ApiResponse;
import com.workflow.compilance.api.dto.response.ComplianceReceiptResponse;
import com.workflow.compilance.api.dto.response.ComplianceReceiptSearchResponse;
import com.workflow.compilance.api.dto.response.CreateComplianceReceiptResponse;
import com.workflow.compilance.application.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/disclosures/v1")
public class ComplianceReceiptController {
    private final CreateComplianceReceiptUseCase useCase;
    private final ObjectProvider<ComplianceReceiptSearchUseCase> searchUseCaseProvider;

    public ComplianceReceiptController(
            CreateComplianceReceiptUseCase useCase,
            ObjectProvider<ComplianceReceiptSearchUseCase> searchUseCaseProvider) {
        this.useCase = useCase;
        this.searchUseCaseProvider = searchUseCaseProvider;
    }

    @PostMapping("/disclosure-receipts")
    public ResponseEntity<ApiResponse> createComplianceReceipt(
            @Valid @RequestBody CreateComplianceReceiptRequest request) {
        CreateComplianceReceiptResult result = useCase.create(CreateComplianceReceiptCommand.fromRequest(request));

        CreateComplianceReceiptResponse payload = new CreateComplianceReceiptResponse(
                result.disclosureReceiptId(),
                result.workActionId(),
                result.customerId(),
                result.status()
        );

        ApiResponse response = new ApiResponse("SUCCESS", HttpStatus.OK.value(), "", List.of(), List.of(), payload);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/disclosure-receipts/search")
    public ResponseEntity<ApiResponse> searchComplianceReceipts(
            @Valid @RequestBody ComplianceReceiptSearchRequest request) {
        ComplianceReceiptSearchResult result = searchUseCaseProvider.getObject()
                .search(ComplianceReceiptSearchCommand.fromRequest(request));

        List<ComplianceReceiptResponse> receipts = result.disclosureReceipts().stream()
                .map(receipt -> new ComplianceReceiptResponse(
                        receipt.disclosureReceiptId(), receipt.workActionId(), receipt.customerId(), receipt.receiptType(),
                        receipt.deliveryChannel(), receipt.recipient(), receipt.receivedAt(), receipt.referenceNumber(),
                        receipt.status()))
                .toList();
        ComplianceReceiptSearchResponse payload = new ComplianceReceiptSearchResponse(receipts);
        String message = receipts.isEmpty() ? "No disclosure receipts found." : "Compliance receipts retrieved successfully.";

        return ResponseEntity.ok(new ApiResponse("SUCCESS", HttpStatus.OK.value(), message, List.of(), List.of(), payload));
    }
}
