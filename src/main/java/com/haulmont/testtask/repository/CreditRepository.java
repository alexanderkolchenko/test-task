package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
