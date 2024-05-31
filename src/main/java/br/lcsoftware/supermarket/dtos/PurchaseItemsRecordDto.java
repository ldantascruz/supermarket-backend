package br.lcsoftware.supermarket.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PurchaseItemsRecordDto(
        @NotNull UUID purchaseId,
        @NotNull UUID itemId,
        @NotNull Integer quantity,
        @NotNull Double unitPrice
) {
}
