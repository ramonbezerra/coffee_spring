package com.example.coffee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



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

    @Autowired
    private CoffeeRepository coffeeRepository;

    // @RequestMapping(value = "/coffees", method = RequestMethod.GET)
    @GetMapping
    public Iterable<Coffee> getCoffees() {
        return coffeeRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Coffee> getMethodName(@PathVariable String id) {
        Optional<Coffee> coffee = coffeeRepository.findById(Long.parseLong(id));
        
        if (coffee.isEmpty())
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(coffee.get());
    }    

    @PostMapping
    @ResponseBody
    public ResponseEntity<Coffee> postCoffee(@RequestBody Coffee coffee) {
        // coffees.add(coffee);
        try {
            return ResponseEntity.ok(coffeeRepository.save(coffee));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
        try {
            return ResponseEntity.ok(coffeeRepository.save(coffee));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoffee(@PathVariable Long id) {
        coffeeRepository.delete(coffeeRepository.findById(id).get());
        return ResponseEntity.noContent().build();
    }
}
