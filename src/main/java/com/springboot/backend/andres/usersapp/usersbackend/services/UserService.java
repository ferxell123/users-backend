package com.springboot.backend.andres.usersapp.usersbackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.springboot.backend.andres.usersapp.usersbackend.entities.User;
import com.springboot.backend.andres.usersapp.usersbackend.pageable.PageResponse;

public interface UserService {
    List<User> findAll();
    PageResponse<User> findAll(Pageable pageable);
    Optional<User> findById(Long id);
    User save( User user);
    void deleteById(@NonNull Long id);

}
