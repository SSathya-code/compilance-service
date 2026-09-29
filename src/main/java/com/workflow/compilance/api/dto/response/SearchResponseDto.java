package com.workflow.compilance.api.dto.response;

import com.workflow.compilance.api.dto.request.ComplianceDto;

import java.util.List;

public record SearchResponseDto(List<ComplianceDto> items, int page, int pageSize,
                                long totalElements, int totalPages) {
}
