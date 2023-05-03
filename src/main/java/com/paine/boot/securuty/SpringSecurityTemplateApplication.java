package com.paine.boot.securuty;


import com.paine.boot.securuty.repositories.RoleRepository;
import com.paine.boot.securuty.repositories.UserRepository;

import com.paine.boot.securuty.entities.Role;
import com.paine.boot.securuty.entities.User;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;


@SpringBootApplication
public class SpringSecurityTemplateApplication implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SpringSecurityTemplateApplication(PasswordEncoder passwordEncoder,
                                            RoleRepository roleRepository,
                                            UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityTemplateApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        roleRepository.save(adminRole);
        roleRepository.save(userRole);
        HashSet<Role> setRoles = new HashSet<>();
        setRoles.add(userRole);

        userRepository.save(new User("Владислав", "Кузнецов", "vladp82k17@gmail.com",
                passwordEncoder.encode("100"), (byte) 25, setRoles));
        userRepository.save(new User("User", "Кузнецов", "user@user.ru",
                passwordEncoder.encode("100"), (byte) 25, setRoles));
        setRoles.add(adminRole);
        userRepository.save(new User("Юрий", "Катков", "marlovo@rambler.ru",
                passwordEncoder.encode("100"), (byte) 25, setRoles));
        userRepository.save(new User("Admin", "Катков", "admin@admin.ru",
                passwordEncoder.encode("100"), (byte) 25, setRoles));
    }
}
