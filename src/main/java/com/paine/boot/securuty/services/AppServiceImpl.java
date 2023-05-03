package com.paine.boot.securuty.services;


import com.paine.boot.securuty.repositories.RoleRepository;
import com.paine.boot.securuty.repositories.UserRepository;
import com.paine.boot.securuty.entities.Role;
import com.paine.boot.securuty.entities.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import java.util.Collection;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class AppServiceImpl implements UserDetailsService, AppService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AppServiceImpl(PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public User insertOrUpdateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User findUserById(Long id) throws IllegalArgumentException {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("User with ID %d not found", id)));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void deleteUserById(Long id) throws IllegalArgumentException {
        userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("User with ID %d could not be delete", id)));
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Username '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRoleToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
