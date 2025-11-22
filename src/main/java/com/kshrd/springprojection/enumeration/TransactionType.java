package com.kshrd.springprojection.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    WITHDRAWAL("withdrawal"),
    DEPOSIT("deposit");
    private final String fieldName;
}
