package com.paine.boot.securuty.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.paine.boot.securuty.entities.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
