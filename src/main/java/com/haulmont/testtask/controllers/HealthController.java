package com.haulmont.testtask.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RestController
@RequestMapping("/health")
public class HealthController


    @PersistenceContext
    protected EntityManager entityManager;

    @GetMapping
    public boolean healthz() {
        return entityManager.createQuery("select 1", Boolean.class).getSingleResult();
    }
}

