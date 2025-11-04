package com.springboot.backend.andres.usersapp.usersbackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.andres.usersapp.usersbackend.entities.User;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(@NonNull User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Long id) {
        userRepository.deleteById(id);
    }

}
