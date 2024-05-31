package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ShoppingListItemsRecordDto(
        @NotNull UUID shoppingListId,
        @NotNull UUID itemId,
        @NotNull Integer quantity,
        @NotNull Double unitPrice
) {
}
