package com.hamdan.slinkapi.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record SlinkRequestDto(
        @NotBlank @URL String url,
        String customAlias
) {
}
