package br.lcsoftware.supermarket.dtos;

import java.util.UUID;

public record UserResponseRecordDto(
        UUID id,
        String email,
        String name
) {
}
