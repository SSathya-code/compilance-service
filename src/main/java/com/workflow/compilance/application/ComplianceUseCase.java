package com.workflow.compilance.application;

import com.workflow.compilance.api.dto.request.ComplianceDto;
import com.workflow.compilance.api.dto.request.ComplianceSearchDto;
import com.workflow.compilance.api.dto.request.CreateComplianceDto;
import com.workflow.compilance.api.dto.response.ApiMessage;
import com.workflow.compilance.api.dto.response.ApiResponse;
import com.workflow.compilance.api.dto.response.SearchResponseDto;
import com.workflow.compilance.application.exception.CompliancePersistenceException;
import com.workflow.compilance.application.exception.DuplicateComplianceException;
import com.workflow.compilance.domain.Compliance;
import com.workflow.compilance.persistence.ComplianceEntity;
import com.workflow.compilance.persistence.ComplianceRepository;
import com.workflow.compilance.persistence.mapper.ComplianceMapper;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplianceUseCase {
    private final ComplianceRepository repository;

    @Transactional
    public ComplianceDto create(CreateComplianceDto request) {
        log.info("Creating disclosure referenceNumber={}", request.referenceNumber());
        if (repository.existsByReferenceNumber(request.referenceNumber())) {
            throw new DuplicateComplianceException(request.referenceNumber());
        }
        try {
            Compliance saved = ComplianceMapper.toDomain(repository.save(ComplianceMapper.toEntity(request)));
            log.info("Compliance created id={} status={}", saved.id(), saved.status());
            return ComplianceMapper.toDto(saved);
        } catch (DataAccessException ex) {
            log.error("Database error while saving disclosure referenceNumber={}", request.referenceNumber(), ex);
            throw new CompliancePersistenceException("Compliance creation failed.", ex);
        }
    }

    @Transactional(readOnly = true)
    public ApiResponse search(ComplianceSearchDto criteria) {
        log.info("Searching disclosures page={} pageSize={}", criteria.effectivePage(), criteria.effectivePageSize());
        try {
            Pageable pageable = PageRequest.of(criteria.effectivePage(), criteria.effectivePageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<ComplianceEntity> result = repository.findAll((root, query, cb) -> {
                var predicates = new ArrayList<Predicate>();
                if (hasText(criteria.referenceNumber()))
                    predicates.add(cb.like(cb.lower(root.get("referenceNumber")), "%" + criteria.referenceNumber().toLowerCase() + "%"));
                if (hasText(criteria.customerId()))
                    predicates.add(cb.equal(root.get("customerId"), criteria.customerId()));
                if (hasText(criteria.status()))
                    predicates.add(cb.equal(root.get("status"), criteria.status().toUpperCase()));
                return cb.and(predicates.toArray(Predicate[]::new));
            }, pageable);
            var items = result.stream().map(ComplianceMapper::toDomain).map(ComplianceMapper::toDto).toList();
            log.info("Compliance search completed totalElements={}", result.getTotalElements());
            var data = new SearchResponseDto(items, result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
            return new ApiResponse("SUCCESS", HttpStatus.OK.value(), "", List.of(), List.of(), data);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid search criteria", ex);
            return new ApiResponse("ERROR", HttpStatus.BAD_REQUEST.value(), ex.getMessage(), List.of(new ApiMessage("INVALID_REQUEST", ex.getMessage())), List.of(), null);
        } catch (DataAccessException | jakarta.persistence.PersistenceException ex) {
            log.error("Database error while searching disclosures", ex);
            return new ApiResponse("ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database error while searching disclosures.", List.of(new ApiMessage("DB_ERROR", ex.getMessage())), List.of(), null);
        } catch (Exception ex) {
            log.error("Unexpected error while searching disclosures", ex);
            return new ApiResponse("ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error while searching disclosures.", List.of(new ApiMessage("UNEXPECTED_ERROR", ex.getMessage())), List.of(), null);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
