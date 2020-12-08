package com.example.coffee.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.io.File;
import java.util.Collections;

import com.example.coffee.builder.CoffeeDTOBuilder;
import com.example.coffee.dto.CoffeeDTO;
import com.example.coffee.exception.CoffeeNotFoundException;
import com.example.coffee.model.Coffee;
import com.example.coffee.service.CoffeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CoffeeControllerTests {

    private static final String COFFEE_API_URL_PATH = "/api/v1/coffees";
    private static final long VALID_COFFEE_ID = 1L;
    private static final long INVALID_COFFEE_ID = 2L;
    
    private MockMvc mvc;
    
    @Mock
    private CoffeeService coffeeService; 

    @InjectMocks
    private CoffeeController coffeeController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(coffeeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledWithValidFieldsThenShouldCreateCoffee() throws Exception { 
        //given   
        String createCoffeeRequest = objectMapper.writeValueAsString(objectMapper.readValue(new File("src/test/resources/createCoffeeValidRequest.json"), Coffee.class));
        CoffeeDTO expectedCoffeeDTO = CoffeeDTOBuilder.builder().build().toCoffeeDTO();
        
        //when
        when(coffeeService.create(expectedCoffeeDTO)).thenReturn(expectedCoffeeDTO);
        
        //then
        mvc.perform(post(COFFEE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(createCoffeeRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenGETIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        CoffeeDTO coffeeDTO = CoffeeDTOBuilder.builder().build().toCoffeeDTO();

        //when
        when(coffeeService.listAll()).thenReturn(Collections.singletonList(coffeeDTO));

        //then
        mvc.perform(get(COFFEE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGETIsCalledWithValidDataThenOkStatusIsReturned() throws Exception {
        // given
        CoffeeDTO coffeeDTO = CoffeeDTOBuilder.builder().build().toCoffeeDTO();

        //when
        when(coffeeService.findById(coffeeDTO.getId())).thenReturn(coffeeDTO);

        //then
        mvc.perform(get(COFFEE_API_URL_PATH + "/" + VALID_COFFEE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(coffeeDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(coffeeDTO.getName())));
    }

    @Test
    void whenDELETEIsCalledWithValidIdThenNoContentStatusIsReturned() throws Exception {
        // given
        CoffeeDTO coffeeDTO = CoffeeDTOBuilder.builder().build().toCoffeeDTO();

        // when
        doNothing().when(coffeeService).deleteById(coffeeDTO.getId());

        // then
        mvc.perform(delete(COFFEE_API_URL_PATH + "/" + VALID_COFFEE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception {
        // when
        doThrow(CoffeeNotFoundException.class).when(coffeeService).deleteById(INVALID_COFFEE_ID);

        // then
        mvc.perform(delete(COFFEE_API_URL_PATH + "/" + INVALID_COFFEE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPUTIsCalledWithValidDataThenOKstatusIsReturned() throws Exception {
        // given
        String updateCoffeeValidRequest = objectMapper.writeValueAsString(objectMapper.readValue(new File("src/test/resources/updateCoffeeValidRequest.json"), Coffee.class));
        CoffeeDTO coffeeDTO = CoffeeDTOBuilder.builder().name("Cappuccino").build().toCoffeeDTO();

        // when
        when(coffeeService.update(VALID_COFFEE_ID, coffeeDTO)).thenReturn(coffeeDTO);

        // then
        mvc.perform(put(COFFEE_API_URL_PATH + "/" + VALID_COFFEE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(updateCoffeeValidRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(coffeeDTO.getName())));
    }

    @Test
    void whenPUTIsCalledWithInvalidDataThenNotFoundStatusIsReturned() throws Exception {
        // given
        String updateCoffeeInvalidRequest = objectMapper.writeValueAsString(objectMapper.readValue(new File("src/test/resources/updateCoffeeInvalidRequest.json"), Coffee.class));
        CoffeeDTO coffeeDTO = CoffeeDTOBuilder.builder().id(INVALID_COFFEE_ID).name("Cappuccino").build().toCoffeeDTO();

        // when
        doThrow(CoffeeNotFoundException.class).when(coffeeService).update(INVALID_COFFEE_ID, coffeeDTO);

        // then
        mvc.perform(put(COFFEE_API_URL_PATH + "/" + INVALID_COFFEE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(updateCoffeeInvalidRequest)))
                .andExpect(status().isNotFound());
    }

}
