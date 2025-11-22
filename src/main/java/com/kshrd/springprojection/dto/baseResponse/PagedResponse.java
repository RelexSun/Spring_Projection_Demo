package com.kshrd.springprojection.dto.baseResponse;

public record PagedResponse<T>(
        T items,
        PaginationInfo pagination
) {
}
