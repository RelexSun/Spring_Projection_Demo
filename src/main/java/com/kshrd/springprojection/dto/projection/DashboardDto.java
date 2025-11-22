package com.kshrd.springprojection.dto.projection;

import java.math.BigDecimal;

public record DashboardDto(
        long totalTransactions,
        BigDecimal totalAmount,
        long depositCount,
        long withdrawalCount
) {
}
