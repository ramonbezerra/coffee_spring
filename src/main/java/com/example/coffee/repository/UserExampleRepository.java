package com.example.coffee.repository;

import com.example.coffee.model.UserExample;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserExampleRepository extends JpaRepository<UserExample, Long> {
    UserExample findByUsername(String username); // select u from UserExample where u.username = ?1
}
