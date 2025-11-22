package com.kshrd.springprojection.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionProperty {
    AMOUNT("amount"), TYPE("type"), TIMESTAMP("timestamp");

    private final String fieldName;
}
