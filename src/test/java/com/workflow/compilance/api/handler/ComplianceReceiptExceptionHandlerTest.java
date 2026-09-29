package com.workflow.compilance.api.handler;

import com.workflow.compilance.api.dto.response.ApiResponse;
import com.workflow.compilance.application.exception.DuplicateComplianceReceiptException;
import com.workflow.compilance.application.exception.TaskNotFoundException;
import com.workflow.compilance.domain.ReceiptType;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ComplianceReceiptExceptionHandlerTest {

    private final ComplianceReceiptExceptionHandler handler = new ComplianceReceiptExceptionHandler();

    @Test
    void shouldReturnStandardErrorEnvelopeForWorkActionNotFound() {
        ResponseEntity<ApiResponse> response = handler.handleWorkActionNotFound(new TaskNotFoundException("WA-404"));

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("ERROR", response.getBody().status());
        assertEquals(404, response.getBody().statusCode());
        assertEquals("ERR-WORK-ACTION-NOT-FOUND", response.getBody().errors().getFirst().code());
        assertEquals("Work action WA-404 does not exist.", response.getBody().message());
        assertEquals("{}", response.getBody().data().toString());
    }

    @Test
    void shouldReturnStandardErrorEnvelopeForDuplicateReceipt() {
        ResponseEntity<ApiResponse> response = handler.handleDuplicateReceipt(
                new DuplicateComplianceReceiptException("WA-100", ReceiptType.DISCLOSURE, "REF-100"));

        assertEquals(409, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("ERROR", response.getBody().status());
        assertEquals(409, response.getBody().statusCode());
        assertEquals("ERR-DUPLICATE-DISCLOSURE-RECEIPT", response.getBody().errors().getFirst().code());
        assertEquals("A disclosure receipt already exists for the provided workActionId, receiptType and referenceNumber.", response.getBody().message());
        assertEquals("{}", response.getBody().data().toString());
    }
}
