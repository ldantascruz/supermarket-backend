package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotNull;

public record ErrorResponseRecordDto(
    @NotNull int statusCode,
    @NotNull String message
) {
}
