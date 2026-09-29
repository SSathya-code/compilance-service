package com.workflow.compilance.infrastructure;

import com.workflow.compilance.application.ComplianceReceiptSearchCommand;
import com.workflow.compilance.domain.ComplianceReceipt;
import com.workflow.compilance.domain.ComplianceReceiptRepository;
import com.workflow.compilance.domain.ReceiptType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Repository
public class ComplianceReceiptRepositoryAdapter implements ComplianceReceiptRepository {
    private final SpringDataComplianceReceiptRepository repository;

    public ComplianceReceiptRepositoryAdapter(SpringDataComplianceReceiptRepository repository) {
        this.repository = repository;
    }

    @Override
    public ComplianceReceipt save(ComplianceReceipt disclosureReceipt) {
        ComplianceReceiptJpaEntity entity = new ComplianceReceiptJpaEntity(
                disclosureReceipt.disclosureReceiptId(),
                disclosureReceipt.workActionId(),
                disclosureReceipt.customerId(),
                disclosureReceipt.receiptType(),
                disclosureReceipt.deliveryChannel(),
                disclosureReceipt.recipient(),
                disclosureReceipt.receivedAt(),
                disclosureReceipt.referenceNumber(),
                disclosureReceipt.notes(),
                disclosureReceipt.status(),
                disclosureReceipt.createdAt(),
                disclosureReceipt.updatedAt()
        );
        ComplianceReceiptJpaEntity saved = repository.save(entity);
        return new ComplianceReceipt(
                saved.getComplianceReceiptId(),
                saved.getWorkActionId(),
                saved.getCustomerId(),
                saved.getReceiptType(),
                saved.getDeliveryChannel(),
                saved.getRecipient(),
                saved.getReceivedAt(),
                saved.getReferenceNumber(),
                saved.getNotes(),
                saved.getStatus(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    @Override
    public boolean existsByWorkActionIdAndReceiptTypeAndReferenceNumber(String workActionId, ReceiptType receiptType, String referenceNumber) {
        return repository.existsByWorkActionIdAndReceiptTypeAndReferenceNumber(workActionId, receiptType, referenceNumber);
    }

    @Override
    public List<ComplianceReceipt> search(ComplianceReceiptSearchCommand command) {
        Specification<ComplianceReceiptJpaEntity> specification = Specification.allOf(Stream.of(
                equalsIfSupplied("workActionId", command.workActionId()),
                equalsIfSupplied("customerId", command.customerId()),
                equalsIfSupplied("receiptType", command.receiptType()),
                equalsIfSupplied("deliveryChannel", command.deliveryChannel()),
                equalsIfSupplied("status", command.status()),
                equalsIfSupplied("referenceNumber", command.referenceNumber()),
                fromReceivedAtIfSupplied(command.fromReceivedAt()),
                toReceivedAtIfSupplied(command.toReceivedAt())
        ).filter(Objects::nonNull).toList());

        return repository.findAll(specification).stream().map(this::toDomain).toList();
    }

    private Specification<ComplianceReceiptJpaEntity> equalsIfSupplied(String attribute, Object value) {
        return value == null ? null : (root, query, builder) -> builder.equal(root.get(attribute), value);
    }

    private Specification<ComplianceReceiptJpaEntity> fromReceivedAtIfSupplied(java.time.Instant fromReceivedAt) {
        return fromReceivedAt == null ? null : (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("receivedAt"), fromReceivedAt);
    }

    private Specification<ComplianceReceiptJpaEntity> toReceivedAtIfSupplied(java.time.Instant toReceivedAt) {
        return toReceivedAt == null ? null : (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("receivedAt"), toReceivedAt);
    }

    private ComplianceReceipt toDomain(ComplianceReceiptJpaEntity entity) {
        return new ComplianceReceipt(
                entity.getComplianceReceiptId(), entity.getWorkActionId(), entity.getCustomerId(),
                entity.getReceiptType(), entity.getDeliveryChannel(), entity.getRecipient(), entity.getReceivedAt(),
                entity.getReferenceNumber(), entity.getNotes(), entity.getStatus(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
