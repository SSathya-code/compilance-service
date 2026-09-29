package com.workflow.compilance.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "disclosures", uniqueConstraints = @UniqueConstraint(name = "uk_disclosure_reference", columnNames = "reference_number"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ComplianceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "reference_number", nullable = false, length = 50)
    private String referenceNumber;
    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;
    @Column(nullable = false, length = 500)
    private String description;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
