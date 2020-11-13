package com.example.coffee.controller;

import com.example.coffee.dto.UserExampleDTO;
import com.example.coffee.model.UserExample;
import com.example.coffee.repository.UserExampleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@CrossOrigin
public class UserExampleController {
    
    @Autowired
    private UserExampleRepository userExampleRepository;

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<UserExample> createUser(@RequestBody UserExampleDTO user) {
        try {
            return ResponseEntity.ok(
                userExampleRepository.save(
                    new UserExample(user.getEmail(), user.getUsername(), user.getPassword())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
}
