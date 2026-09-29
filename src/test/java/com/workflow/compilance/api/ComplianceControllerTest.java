package com.workflow.compilance.api;

import com.workflow.compilance.application.ComplianceUseCase;
import com.workflow.compilance.application.ReceiptDisplayUseCase;
import com.workflow.compilance.i18n.MessageResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComplianceController.class)
class ComplianceControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ComplianceUseCase useCase;
    @MockitoBean
    ReceiptDisplayUseCase receiptDisplayUseCase;
    @MockitoBean
    MessageResolver messages;

    @Test
    void greetReturnsLocalizedGreeting() throws Exception {
        when(messages.get(any(), any(Locale.class))).thenReturn("Welcome to the WORKFLOW Compliance Service.");
        mockMvc.perform(get("/api/disclosures/v1/greet").header("Accept-Language", "en"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the WORKFLOW Compliance Service."));
    }
}
