package com.kshrd.springprojection.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotBlank(message = "Type cannot be blank")
        @Pattern(regexp = "DEPOSIT|WITHDRAWAL", message = "Type must be DEPOSIT or WITHDRAWAL")
        String type,

        @NotNull(message = "Account ID cannot be null")
        Long accountId
) {}
