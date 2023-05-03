package com.paine.boot.securuty.configs;


import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;


@Configuration
public class PersistenceHibernate {
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
