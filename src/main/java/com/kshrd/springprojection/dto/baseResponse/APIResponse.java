package com.kshrd.springprojection.dto.baseResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public record APIResponse<T>(
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) T payload,
        HttpStatus status,
        Instant instant
) {}
