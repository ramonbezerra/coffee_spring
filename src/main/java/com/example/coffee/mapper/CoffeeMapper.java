package com.example.coffee.mapper;

import com.example.coffee.dto.CoffeeDTO;
import com.example.coffee.model.Coffee;

// import org.mapstruct.Mapper;
// import org.mapstruct.factory.Mappers;

// @Mapper
public class CoffeeMapper {
    // CoffeeMapper INSTANCE = Mappers.getMapper(CoffeeMapper.class);

    public static Coffee toModel(CoffeeDTO coffeeDTO) {
        return new Coffee(coffeeDTO.getId(), coffeeDTO.getName());
    }

    public static CoffeeDTO toDTO(Coffee coffee) {
        return new CoffeeDTO(coffee.getId(), coffee.getName());
    }
}
