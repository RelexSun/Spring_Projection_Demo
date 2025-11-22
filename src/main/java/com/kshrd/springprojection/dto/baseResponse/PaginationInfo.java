package com.kshrd.springprojection.dto.baseResponse;

public record PaginationInfo(
        long totalElements,
        int currentPage,
        int pageSize,
        int totalPages
) {
}
