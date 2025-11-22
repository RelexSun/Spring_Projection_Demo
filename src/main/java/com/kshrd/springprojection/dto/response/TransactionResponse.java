package com.kshrd.springprojection.dto.response;

import com.kshrd.springprojection.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        String type,
        BigDecimal amount,
        LocalDateTime timestamp,
        Long accountId,
        AccountResponse accountOwner
) {}
