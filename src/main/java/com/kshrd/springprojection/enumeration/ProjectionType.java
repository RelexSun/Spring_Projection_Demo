package com.kshrd.springprojection.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectionType {
    SUMMARY("summary"),
    WITH_ACCOUNT("with_account");

    private final String fieldName;
}
