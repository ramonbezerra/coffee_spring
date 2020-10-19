package com.example.coffee;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coffee {
    private final String id;
    private String name;

    public Coffee(String name) {
        this(UUID.randomUUID().toString(), name);
    }
}
