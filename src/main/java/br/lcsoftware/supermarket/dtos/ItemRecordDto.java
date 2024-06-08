package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemRecordDto(
        @NotNull UUID shoppingListId,
        @NotBlank String name,
        Integer quantity,
        Float unitPrice
) {}
