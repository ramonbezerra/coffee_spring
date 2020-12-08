package com.example.coffee.controller;

import java.util.List;

import javax.validation.Valid;

import com.example.coffee.dto.CoffeeDTO;
import com.example.coffee.exception.CoffeeAlreadyRegisteredException;
import com.example.coffee.exception.CoffeeNotFoundException;
import com.example.coffee.service.CoffeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/coffees")
@Api(value = "Coffee")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CoffeeController {
    
    private final CoffeeService coffeeService;
    
    @GetMapping
    @ApiOperation(value = "Busca uma lista de todos os cafés")
    public List<CoffeeDTO> getCoffees() {
        return coffeeService.listAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Busca um café pelo seu identificador")
    public CoffeeDTO getCoffeeById(@PathVariable String id) throws CoffeeNotFoundException {
        return coffeeService.findById(Long.parseLong(id));
    }
    
    // todo implementar busca @RequestParam(value="name") String name
    // @GetMapping("/{name}")
    // @ApiOperation(value = "Busca um café pelo seu identificador")
    // public CoffeeDTO getCoffeeByName(@PathVariable String name) throws CoffeeNotFoundException {
    //     return coffeeService.findByName(name);
    // }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Cria um novo café")
    public CoffeeDTO postCoffee(@RequestBody @Valid CoffeeDTO coffeeDTO) throws CoffeeAlreadyRegisteredException {
        return coffeeService.create(coffeeDTO);        
    }

    @PutMapping("/{id}")
    public CoffeeDTO putCoffee(@PathVariable String id, @RequestBody @Valid CoffeeDTO coffeeDTO) throws CoffeeNotFoundException, CoffeeAlreadyRegisteredException {
        return coffeeService.update(Long.parseLong(id), coffeeDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoffee(@PathVariable String id) throws CoffeeNotFoundException {
        coffeeService.deleteById(Long.parseLong(id));
    }
}
