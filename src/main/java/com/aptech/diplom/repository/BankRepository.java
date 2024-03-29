package com.aptech.diplom.repository;

import com.aptech.diplom.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BankRepository extends JpaRepository<Bank, UUID> {
}
