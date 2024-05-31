package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemsRecordDto(
        @NotBlank @NotNull String name
) {
}
