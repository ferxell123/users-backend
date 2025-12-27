package com.springboot.backend.andres.usersapp.usersbackend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.backend.andres.usersapp.usersbackend.entities.Role;
import com.springboot.backend.andres.usersapp.usersbackend.entities.User;
import com.springboot.backend.andres.usersapp.usersbackend.models.IUser;
import com.springboot.backend.andres.usersapp.usersbackend.models.UserRequest;
import com.springboot.backend.andres.usersapp.usersbackend.pageable.PageResponse;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.RoleRepository;
import com.springboot.backend.andres.usersapp.usersbackend.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map(user -> {
         boolean isAdmin = user.getRoles().stream()
                 .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
         user.setAdmin(isAdmin);
         return user;
        }).toList();
        
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<User> findAll(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable).map( user -> {
            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
            user.setAdmin(isAdmin);
            return user;
        });
        return PageResponse.of(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id.longValue());
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user.getId() == null || user.getId() == 0) {
            user.setId(null);
        }
        user.setRoles( getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }


    @Transactional
    @Override
    public Optional<User> update(UserRequest user, Integer id) {
        Optional<User> existingUser = userRepository.findById(id.longValue());
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setLastname(user.getLastname());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setUsername(user.getUsername());
            
            updatedUser.setRoles(getRoles(user));
            userRepository.save(updatedUser);
            return Optional.of(updatedUser);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteById(@NonNull Integer id) {
        userRepository.deleteById(id.longValue());
    }

    private List<Role> getRoles(IUser user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        // optionalRoleUser.ifPresent(role -> roles.add(role));
        optionalRoleUser.ifPresent(roles::add);
        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        }
        return roles;
    }

}
