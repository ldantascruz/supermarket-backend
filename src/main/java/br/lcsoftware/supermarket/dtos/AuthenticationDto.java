package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationDto(
        @NotNull @NotBlank String email,
        @NotNull @NotBlank String password
) {
}