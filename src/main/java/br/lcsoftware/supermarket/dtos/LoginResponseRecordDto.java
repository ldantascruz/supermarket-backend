package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LoginResponseRecordDto(
        @NotNull UUID id,
        @NotBlank String email,
        @NotBlank String name,
        @NotBlank String token
) {

}
