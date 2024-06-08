package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ShoppingListRecordDto(
        @NotBlank String name,
        @NotNull UUID userId
) {}
