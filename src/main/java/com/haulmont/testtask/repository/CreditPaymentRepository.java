package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.CreditPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface CreditPaymentRepository extends JpaRepository<CreditPayment, UUID> {

    @Query("from CreditPayment where co_cp_id = ?1")
    List<CreditPayment> findCreditPaymentsBycci(UUID co_cp_id);

    @Transactional
    @Modifying
    @Query("delete from CreditPayment where co_cp_id = ?1 ")
    void deleteCreditPaymentById(UUID id);

   /* @Query("delete from CreditPayment where CreditPayment.co_cp_id = CreditOffer.customer_id and CreditPayment.co_cp_id = ?1")
    void deleteCreditOfferWithPayments(UUID id);*/
}
