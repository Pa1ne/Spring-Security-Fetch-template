package com.paine.boot.securuty.services;


import com.paine.boot.securuty.entities.Role;
import com.paine.boot.securuty.entities.User;

import java.util.List;


public interface AppService {

    User insertOrUpdateUser(User user);

    List<User> findAllUsers();

    Iterable<Role> findAllRoles();

    User findUserById(Long id);

    User findByUsername(String username);

    void deleteUserById(Long id);
}