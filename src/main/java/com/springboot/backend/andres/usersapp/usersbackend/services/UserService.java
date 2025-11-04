package com.springboot.backend.andres.usersapp.usersbackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import com.springboot.backend.andres.usersapp.usersbackend.entities.User;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(@NonNull Long id);
    User save(@NonNull User user);
    void deleteById(@NonNull Long id);

}
