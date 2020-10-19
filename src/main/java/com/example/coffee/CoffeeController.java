package com.example.coffee;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coffees")
public class CoffeeController {
    private List<Coffee> coffees = new ArrayList<>();

    public CoffeeController() {
        coffees.addAll(List.of(
            new Coffee("Café Expresso"),
            new Coffee("Café Com Leite"),
            new Coffee("Café Pingado"),
            new Coffee("Café Capuccino")
        ));
    }

    // @RequestMapping(value = "/coffees", method = RequestMethod.GET)
    @GetMapping
    public Iterable<Coffee> getCoffees() {
        return coffees;
    }

    @PostMapping
    public Coffee postCoffee(@RequestBody Coffee coffee) {
        coffees.add(coffee);
        return coffee;
    }
}
