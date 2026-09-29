package com.workflow.compilance.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ComplianceRepository extends JpaRepository<ComplianceEntity, Long>, JpaSpecificationExecutor<ComplianceEntity> {
    boolean existsByReferenceNumber(String referenceNumber);
}
