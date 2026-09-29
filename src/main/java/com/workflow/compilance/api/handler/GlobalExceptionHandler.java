package com.workflow.compilance.api.handler;

import com.workflow.compilance.api.dto.response.ApiMessage;
import com.workflow.compilance.api.dto.response.ApiResponse;
import com.workflow.compilance.application.exception.CompliancePersistenceException;
import com.workflow.compilance.application.exception.DuplicateComplianceException;
import com.workflow.compilance.i18n.MessageKey;
import com.workflow.compilance.i18n.MessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageResolver messages;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.info("Handling request validation failure fieldErrorCount={}", ex.getBindingResult().getFieldErrorCount());
        List<ApiMessage> errors = ex.getBindingResult().getFieldErrors().stream()
                .sorted(Comparator
                        .comparing((org.springframework.validation.FieldError e) -> isDerivedValidationField(e.getField()))
                        .thenComparing(org.springframework.validation.FieldError::getField))
                .map(e -> new ApiMessage("ERR-VALIDATION-" + e.getField().toUpperCase(), e.getDefaultMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, messages.get(MessageKey.VALIDATION_FAILED, LocaleContextHolder.getLocale()), errors);
    }

    @ExceptionHandler(DuplicateComplianceException.class)
    public ResponseEntity<ApiResponse> handleDuplicate(DuplicateComplianceException ex) {
        log.info("Handling duplicate disclosure error code={}", ex.getErrorCode());
        String message = messages.get(MessageKey.DUPLICATE, LocaleContextHolder.getLocale());
        return build(HttpStatus.CONFLICT, message, List.of(new ApiMessage(ex.getErrorCode(), message)));
    }

    @ExceptionHandler(CompliancePersistenceException.class)
    public ResponseEntity<ApiResponse> handlePersistence(CompliancePersistenceException ex) {
        log.error("Handling disclosure persistence error code={}", ex.getErrorCode(), ex);
        String message = messages.get(MessageKey.PERSISTENCE_ERROR, LocaleContextHolder.getLocale());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, message, List.of(new ApiMessage(ex.getErrorCode(), message)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnexpected(Exception ex) {
        log.error("Handling unexpected error", ex);
        String message = messages.get(MessageKey.UNEXPECTED_ERROR, LocaleContextHolder.getLocale());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, message, List.of(new ApiMessage("ERR-INTERNAL", message)));
    }

    private ResponseEntity<ApiResponse> build(HttpStatus status, String message, List<ApiMessage> errors) {
        var body = new ApiResponse("ERROR", status.value(), message, errors, List.of(), Map.of());
        return ResponseEntity.status(status).body(body);
    }

    private boolean isDerivedValidationField(String field) {
        return field.endsWith("ValidForDeliveryChannel") || field.endsWith("NotInFuture")
                || field.endsWith("CriteriaProvided") || field.endsWith("RangeValid");
    }
}
