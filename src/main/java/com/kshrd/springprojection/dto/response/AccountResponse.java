package com.kshrd.springprojection.dto.response;

public record AccountResponse(
        Long id,
        String accountNumber,
        String holderName
) {
}
