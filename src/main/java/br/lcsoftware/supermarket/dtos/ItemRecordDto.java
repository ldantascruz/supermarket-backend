package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemRecordDto(
        @NotBlank @NotNull String name,
        Integer quantity,
        @NotNull UUID shoppingListId,
        Float unitPrice
) {
}
