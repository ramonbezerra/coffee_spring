package com.example.coffee.service;

import com.example.coffee.dto.CoffeeDTO;
import com.example.coffee.exception.CoffeeAlreadyRegisteredException;
import com.example.coffee.exception.CoffeeNotFoundException;
import com.example.coffee.mapper.CoffeeMapper;
import com.example.coffee.model.Coffee;
import com.example.coffee.repository.CoffeeRepository;
import com.example.coffee.builder.CoffeeDTOBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CoffeeServiceTest {

    @Mock
    private CoffeeRepository coffeeRepository;

    @InjectMocks
    private CoffeeService coffeeService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void whenCoffeeInformedThenItShouldBeCreated() throws CoffeeAlreadyRegisteredException {
        // given
        CoffeeDTO expectedCoffeeDTO = CoffeeDTOBuilder.builder().build().toCoffeeDTO();
        Coffee expectedSavedCoffee = CoffeeMapper.toModel(expectedCoffeeDTO);

        // when
        when(coffeeRepository.findByName(expectedCoffeeDTO.getName())).thenReturn(Optional.empty());
        when(coffeeRepository.save(expectedSavedCoffee)).thenReturn(expectedSavedCoffee);

        //then
        CoffeeDTO createdCoffeeDTO = coffeeService.create(expectedCoffeeDTO);

        assertThat(createdCoffeeDTO.getId(), is(equalTo(expectedCoffeeDTO.getId())));
        assertThat(createdCoffeeDTO.getName(), is(equalTo(expectedCoffeeDTO.getName())));
    }

    @Test
    void whenAlreadyRegisteredBeerInformedThenAnExceptionShouldBeThrown() {
        //given
        CoffeeDTO expectedCoffeeDTO = CoffeeDTOBuilder.builder().build().toCoffeeDTO();
        Coffee duplicatedCoffee = CoffeeMapper.toModel(expectedCoffeeDTO);

        //when
        when(coffeeRepository.findByName(expectedCoffeeDTO.getName())).thenReturn(Optional.of(duplicatedCoffee));

        //then
        assertThrows(CoffeeAlreadyRegisteredException.class, () -> coffeeService.create(expectedCoffeeDTO));
    }

    @Test
    void whenValidCoffeeNameIsGivenThenReturnACoffee() throws CoffeeNotFoundException {
        // given
        CoffeeDTO expectedFoundCoffeeDTO = CoffeeDTOBuilder.builder().build().toCoffeeDTO();
        Coffee expectedFoundCoffee = CoffeeMapper.toModel(expectedFoundCoffeeDTO);

        //when
        when(coffeeRepository.findByName(expectedFoundCoffeeDTO.getName())).thenReturn(Optional.of(expectedFoundCoffee));

        //then
        CoffeeDTO foundCoffeeDTO = coffeeService.findByName(expectedFoundCoffeeDTO.getName());

        assertThat(foundCoffeeDTO, is(equalTo(expectedFoundCoffeeDTO)));
    }

    @Test
    void whenNotRegisteredCoffeeNameIsGivenThenThrowAnException() {
        //given
        CoffeeDTO expectedFoundCoffeeDTO = CoffeeDTOBuilder.builder().build().toCoffeeDTO();

        //when
        when(coffeeRepository.findByName(expectedFoundCoffeeDTO.getName())).thenReturn(Optional.empty());

        //then
        assertThrows(CoffeeNotFoundException.class, () -> coffeeService.findByName(expectedFoundCoffeeDTO.getName()));
    }

    @Test
    void whenListCoffeeIsCalledThenReturnAnEmptyListOfCoffees() { 
        //when
        when(coffeeRepository.findAll()).thenReturn(Collections.emptyList());

        //then
        List<CoffeeDTO> foundListCoffeesDTO = coffeeService.listAll();

        assertThat(foundListCoffeesDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenACoffeeShouldBeDeleted() throws CoffeeNotFoundException {
        //given
        CoffeeDTO expectedDeletedCoffeeDTO = CoffeeDTOBuilder.builder().build().toCoffeeDTO();
        Coffee expectedDeletedCoffee = CoffeeMapper.toModel(expectedDeletedCoffeeDTO);

        //when
        when(coffeeRepository.findById(expectedDeletedCoffeeDTO.getId())).thenReturn(Optional.of(expectedDeletedCoffee));
        doNothing().when(coffeeRepository).deleteById(expectedDeletedCoffeeDTO.getId());

        //then
        coffeeService.deleteById(expectedDeletedCoffeeDTO.getId());

        verify(coffeeRepository, times(1)).findById(expectedDeletedCoffeeDTO.getId());
        verify(coffeeRepository, times(1)).deleteById(expectedDeletedCoffeeDTO.getId());
    }
}
