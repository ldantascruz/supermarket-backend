package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserRecordDto(@NotBlank String name, @NotBlank String email, @NotBlank String password, @NotBlank String cpf) {
}
