package com.springboot.backend.andres.usersapp.usersbackend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.andres.usersapp.usersbackend.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
}
