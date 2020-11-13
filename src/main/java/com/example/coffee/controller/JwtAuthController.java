package com.example.coffee.controller;

import com.example.coffee.dto.JwtResponseDTO;
import com.example.coffee.dto.UserExampleDTO;
import com.example.coffee.security.JwtTokenUtil;
import com.example.coffee.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtAuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtService;

    @PostMapping("/authenticate")
    @ResponseBody
    public ResponseEntity<JwtResponseDTO> createAuthenticationToken(@RequestBody UserExampleDTO authenticationRequest) throws Exception {        
        final UserDetails userDetails = jwtService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
