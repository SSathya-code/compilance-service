package com.workflow.compilance.persistence.mapper;

import com.workflow.compilance.api.dto.request.ComplianceDto;
import com.workflow.compilance.api.dto.request.CreateComplianceDto;
import com.workflow.compilance.domain.Compliance;
import com.workflow.compilance.persistence.ComplianceEntity;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public final class ComplianceMapper {
    private ComplianceMapper() {
    }

    public static ComplianceEntity toEntity(CreateComplianceDto dto) {
        log.info("Mapping create disclosure request for referenceNumber={}", dto.referenceNumber());
        return new ComplianceEntity(null, dto.referenceNumber(), dto.customerId(), dto.description(), "DRAFT", Instant.now());
    }

    public static Compliance toDomain(ComplianceEntity entity) {
        return new Compliance(entity.getId(), entity.getReferenceNumber(), entity.getCustomerId(), entity.getDescription(), entity.getStatus(), entity.getCreatedAt());
    }

    public static ComplianceDto toDto(Compliance domain) {
        return new ComplianceDto(domain.id(), domain.referenceNumber(), domain.customerId(), domain.description(), domain.status(), domain.createdAt());
    }
}
