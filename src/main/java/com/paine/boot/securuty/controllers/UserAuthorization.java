package com.paine.boot.securuty.controllers;


import com.paine.boot.securuty.services.AppServiceImpl;
import com.paine.boot.securuty.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;

import java.security.Principal;


@Controller
public class UserAuthorization {
    private final AppServiceImpl appService;

    @Autowired
    public UserAuthorization(AppServiceImpl appService) {
        this.appService = appService;
    }

    @GetMapping("/")
    public String main(Principal principal, Model model) {
        User user = appService.findByUsername(principal.getName());
        model.addAttribute("authorizedUser", user);
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            return "admin-page";
        }
        return "user-page";
    }
}