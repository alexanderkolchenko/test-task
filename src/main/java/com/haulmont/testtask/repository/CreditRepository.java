package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface CreditRepository extends JpaRepository<Credit, UUID> {

}
