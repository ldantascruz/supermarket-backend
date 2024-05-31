package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PurchasesRecordDto(
        @NotNull UUID userId,
        @NotNull UUID shoppingListId,
        @NotNull Double totalPrice
) {
}
