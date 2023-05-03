package com.paine.boot.securuty.controllers;


import com.paine.boot.securuty.services.AppServiceImpl;
import com.paine.boot.securuty.entities.Role;
import com.paine.boot.securuty.entities.User;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;


@RestController
public class AdminController {

    private final AppServiceImpl appService;

    @Autowired
    public AdminController(AppServiceImpl appService) {
        this.appService = appService;
    }

    @DeleteMapping("/crud/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        appService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/crud")
    public ResponseEntity<User> insert(@RequestBody User user) {
        return ResponseEntity.ok(appService.insertOrUpdateUser(user));
    }

    @GetMapping("/crud/{id}")
    public ResponseEntity<User> showUserProfileModal(@PathVariable Long id) {
        return ResponseEntity.ok(appService.findUserById(id));
    }

    @GetMapping("/crud")
    public ResponseEntity<List<User>> findAllUsers() {
        return ResponseEntity.ok(appService.findAllUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<Iterable<Role>> findAllRoles() {
        return ResponseEntity.ok(appService.findAllRoles());
    }
}