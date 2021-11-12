package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.CreditPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CreditPaymentRepository extends JpaRepository<CreditPayment, UUID> {

    @Query("from CreditPayment where co_cp_id = ?1")
    List<CreditPayment> findCreditPaymentsBycci(UUID co_cp_id);
}
