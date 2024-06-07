package br.lcsoftware.supermarket.dtos;

import java.util.UUID;

public record ItemResponseRecordDto(
        UUID id,
        UUID shoppingListId,
        String name,
        Integer quantity,
        Float unitPrice
) {
}
