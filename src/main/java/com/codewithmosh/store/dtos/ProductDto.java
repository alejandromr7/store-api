package com.codewithmosh.store.dtos;


public record ProductDto(
        Long id,
        String name,
        String description,
        Double price,
        Byte categoryId
) {
}
