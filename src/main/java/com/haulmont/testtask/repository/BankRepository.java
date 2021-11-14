package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
/**
 * интерфейс реализует взаимодействие объектов Bank с базой данных
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */

public interface BankRepository extends JpaRepository<Bank, UUID> {
}
