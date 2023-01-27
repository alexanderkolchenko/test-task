package com.aptech.diplom.repository;

import com.aptech.diplom.models.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
