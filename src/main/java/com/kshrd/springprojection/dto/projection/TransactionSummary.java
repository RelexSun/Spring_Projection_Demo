package com.kshrd.springprojection.dto.projection;

import java.math.BigDecimal;

public interface TransactionSummary {
    Long getId();
    BigDecimal getAmount();
    String getType();
}
