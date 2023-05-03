package com.paine.boot.securuty.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.paine.boot.securuty.entities.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}