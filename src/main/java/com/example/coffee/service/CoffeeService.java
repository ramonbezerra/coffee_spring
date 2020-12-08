package com.example.coffee.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.coffee.dto.CoffeeDTO;
import com.example.coffee.exception.CoffeeAlreadyRegisteredException;
import com.example.coffee.exception.CoffeeNotFoundException;
import com.example.coffee.mapper.CoffeeMapper;
import com.example.coffee.model.Coffee;
import com.example.coffee.repository.CoffeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CoffeeService {

        private final CoffeeRepository coffeeRepository;
        // private final CoffeeMapper coffeeMapper = CoffeeMapper.INSTANCE;

        public List<CoffeeDTO> listAll() {
                return coffeeRepository.findAll().stream().map(CoffeeMapper::toDTO).collect(Collectors.toList());
        }

        public CoffeeDTO findById(Long id) throws CoffeeNotFoundException {
                Coffee foundCoffee = coffeeRepository.findById(id).orElseThrow(() -> new CoffeeNotFoundException(id));

                return CoffeeMapper.toDTO(foundCoffee);
        }

        public CoffeeDTO findByName(String name) throws CoffeeNotFoundException {
                Coffee foundCoffee = coffeeRepository.findByName(name)
                                .orElseThrow(() -> new CoffeeNotFoundException(name));

                return CoffeeMapper.toDTO(foundCoffee);
        }

        public CoffeeDTO create(@Valid CoffeeDTO coffeeDTO) throws CoffeeAlreadyRegisteredException {
                verifyIfIsAlreadyRegistered(coffeeDTO.getName());
                Coffee coffee = CoffeeMapper.toModel(coffeeDTO);
                Coffee savedCoffee = coffeeRepository.save(coffee);
                return CoffeeMapper.toDTO(savedCoffee);
        }

        private void verifyIfIsAlreadyRegistered(String name) throws CoffeeAlreadyRegisteredException {
                Optional<Coffee> optSavedCoffee = coffeeRepository.findByName(name);
                if (optSavedCoffee.isPresent())
                        throw new CoffeeAlreadyRegisteredException(name);
        }

        public void deleteById(Long id) throws CoffeeNotFoundException {
                Coffee coffeeToDelete = verifyIfExists(id);
                coffeeRepository.deleteById(coffeeToDelete.getId());
        }

        private Coffee verifyIfExists(Long id) throws CoffeeNotFoundException {
                return coffeeRepository.findById(id).orElseThrow(() -> new CoffeeNotFoundException(id));
        }

        public CoffeeDTO update(Long id, CoffeeDTO coffeeDTO)
                        throws CoffeeNotFoundException, CoffeeAlreadyRegisteredException {
                Coffee coffeeToUpdate = verifyIfExists(id);
                verifyIfIsAlreadyRegistered(coffeeDTO.getName());
                Coffee savedCoffee = coffeeRepository.save(new Coffee(coffeeToUpdate.getId(), coffeeDTO.getName()));
                return CoffeeMapper.toDTO(savedCoffee);
        }

}
