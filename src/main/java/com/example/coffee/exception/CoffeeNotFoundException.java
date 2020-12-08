package com.example.coffee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CoffeeNotFoundException extends Exception {
    
    private static final long serialVersionUID = 5486937127569898244L;

    public CoffeeNotFoundException(String coffeeName) {
        super(String.format("Coffee with name %s not found in the system.", coffeeName));
    }

    public CoffeeNotFoundException(Long id) {
        super(String.format("Coffee with id %s not found in the system.", id));
    }
}
