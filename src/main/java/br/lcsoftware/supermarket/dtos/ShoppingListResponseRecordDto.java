package br.lcsoftware.supermarket.dtos;

import java.util.List;
import java.util.UUID;

public record ShoppingListResponseRecordDto(
        UUID id,
        String name,
        List<ItemResponseRecordDto> items,
        UserResponseRecordDto user
) {
}
