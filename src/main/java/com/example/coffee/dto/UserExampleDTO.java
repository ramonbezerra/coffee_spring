package com.example.coffee.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExampleDTO {
    private String email;
    private String username;
    private String password;
}
