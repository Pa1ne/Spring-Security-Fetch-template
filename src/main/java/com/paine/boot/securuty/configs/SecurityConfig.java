package com.paine.boot.securuty.configs;


import com.paine.boot.securuty.services.AppServiceImpl;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.context.annotation.Bean;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AppServiceImpl appService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder,
                          AppServiceImpl appService) {
        this.passwordEncoder = passwordEncoder;
        this.appService = appService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                    .requestMatchers("/webjars/**", "/actuator/**").permitAll()
                    //.requestMatchers("/crud/**").hasRole("ADMIN")
                    //.requestMatchers("/roles/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                )
                .formLogin()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login");
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(appService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
}