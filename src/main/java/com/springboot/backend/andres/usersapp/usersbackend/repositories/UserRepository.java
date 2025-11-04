package com.springboot.backend.andres.usersapp.usersbackend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.springboot.backend.andres.usersapp.usersbackend.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
