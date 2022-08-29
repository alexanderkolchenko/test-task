package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BankRepository extends JpaRepository<Bank, UUID> {
}
