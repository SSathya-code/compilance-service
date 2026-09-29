package com.workflow.compilance.api;

import com.workflow.compilance.application.CreateComplianceReceiptResult;
import com.workflow.compilance.application.CreateComplianceReceiptUseCase;
import com.workflow.compilance.domain.ComplianceReceiptStatus;
import com.workflow.compilance.i18n.MessageResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComplianceReceiptController.class)
class ComplianceReceiptControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateComplianceReceiptUseCase useCase;

    @MockitoBean
    private MessageResolver messageResolver;

    @BeforeEach
    void setUp() {
        when(messageResolver.get(any(), any(), any())).thenReturn("Validation failed");
    }

    @Test
    void shouldCreateComplianceReceiptSuccessfully() throws Exception {
        String jsonBody = """
                {
                  "workActionId": "WA-100",
                  "customerId": "CUST-100",
                  "receiptType": "DISCLOSURE",
                  "deliveryChannel": "EMAIL",
                  "recipient": "customer@example.com",
                  "receivedAt": "2026-08-28T00:00:00Z",
                  "referenceNumber": "REF-100",
                  "notes": "Acknowledged"
                }
                """;

        when(useCase.create(any())).thenReturn(new CreateComplianceReceiptResult(
                "DR-123",
                "WA-100",
                "CUST-100",
                ComplianceReceiptStatus.CREATED
        ));

        mockMvc.perform(post("/api/disclosures/v1/disclosure-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.disclosureReceiptId").value("DR-123"))
                .andExpect(jsonPath("$.data.workActionId").value("WA-100"))
                .andExpect(jsonPath("$.data.customerId").value("CUST-100"))
                .andExpect(jsonPath("$.data.status").value("CREATED"));

        verify(useCase).create(any());
    }

    @Test
    void shouldRejectWhenWorkActionIdIsMissing() throws Exception {
        String jsonBody = """
                {
                  "customerId": "CUST-100",
                  "receiptType": "DISCLOSURE",
                  "deliveryChannel": "EMAIL",
                  "recipient": "[REDACTED_EMAIL_ADDRESS_1]",
                  "receivedAt": "2026-08-28T00:00:00Z",
                  "referenceNumber": "REF-100"
                }
                """;

        mockMvc.perform(post("/api/disclosures/v1/disclosure-receipts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors[0].code").value("ERR-VALIDATION-WORKACTIONID"))
                .andExpect(jsonPath("$.errors[0].message").value("workActionId is mandatory"));

        verifyNoInteractions(useCase);
    }
}
