package com.workflow.compilance.api;

import com.workflow.compilance.api.dto.request.ComplianceDto;
import com.workflow.compilance.api.dto.request.ComplianceSearchDto;
import com.workflow.compilance.api.dto.request.CreateComplianceDto;
import com.workflow.compilance.api.dto.request.ReceiptDisplayRequest;
import com.workflow.compilance.api.dto.response.ApiResponse;
import com.workflow.compilance.api.dto.response.ReceiptDisplayResponse;
import com.workflow.compilance.application.ComplianceUseCase;
import com.workflow.compilance.application.ReceiptDisplayUseCase;
import com.workflow.compilance.i18n.MessageKey;
import com.workflow.compilance.i18n.MessageResolver;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/disclosures/v1")
@RequiredArgsConstructor
public class ComplianceController {
    private final ComplianceUseCase useCase;
    private final ReceiptDisplayUseCase receiptDisplayUseCase;
    private final MessageResolver messages;

    @GetMapping("/greet")
    public ResponseEntity<String> greet() {
        log.info("Processing greet request");
        return ResponseEntity.ok(messages.get(MessageKey.GREETING, LocaleContextHolder.getLocale()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateComplianceDto request) {
        log.info("Processing create disclosure request referenceNumber={}", request.referenceNumber());
        ComplianceDto created = useCase.create(request);
        ApiResponse response = new ApiResponse("SUCCESS", HttpStatus.OK.value(), "", List.of(), List.of(), created);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse> search(@Valid @RequestBody ComplianceSearchDto request) {
        log.info("Processing disclosure search request");
        ApiResponse response = useCase.search(request);
        return ResponseEntity.status(HttpStatus.valueOf(response.statusCode())).body(response);
    }

    @PostMapping("/receipt/display")
    public ResponseEntity<ApiResponse> displayReceipt(@Valid @RequestBody ReceiptDisplayRequest request) {
        ReceiptDisplayResponse result = receiptDisplayUseCase.display(request);
        ApiResponse response = new ApiResponse("SUCCESS", HttpStatus.ACCEPTED.value(),
                "Receipt display event published.", List.of(), List.of(), result);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
