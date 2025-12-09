package com.springboot.backend.andres.usersapp.usersbackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.springboot.backend.andres.usersapp.usersbackend.entities.User;
import com.springboot.backend.andres.usersapp.usersbackend.models.UserRequest;
import com.springboot.backend.andres.usersapp.usersbackend.pageable.PageResponse;

public interface UserService {
    List<User> findAll();
    PageResponse<User> findAll(Pageable pageable);
    Optional<User> findById(Integer id);
    User save( User user);
    Optional<User> update(UserRequest user, Integer id);
    void deleteById(@NonNull Integer id);
    //Integer deleteById(@NonNull Integer id);

}
