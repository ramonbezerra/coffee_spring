package com.example.coffee.builder;

import com.example.coffee.dto.CoffeeDTO;

import lombok.Builder;

@Builder
public class CoffeeDTOBuilder {
    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Expresso";

    public CoffeeDTO toCoffeeDTO() {
        return new CoffeeDTO(id, name);
    }
}
