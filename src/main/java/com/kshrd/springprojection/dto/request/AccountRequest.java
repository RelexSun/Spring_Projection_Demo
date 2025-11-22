package com.kshrd.springprojection.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountRequest(
        @NotBlank(message = "Holder name cannot be blank")
        @Size(max = 100, message = "Holder name cannot exceed 100 characters")
        String holderName
) {}
