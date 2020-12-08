package com.example.coffee.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeDTO {
    
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;
}
