package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.UUID;

public interface CreditOfferRepository extends JpaRepository<CreditOffer, UUID> {
    @Transactional
    @Modifying
    @Query("delete from CreditOffer where customer_id = ?1")
    void deleteCreditOfferByCustomer(UUID customerId);

    @Transactional
    @Modifying
    @Query("delete from CreditOffer where credit_id = ?1")
    void deleteCreditOfferByCredit(UUID creditId);

    @Transactional
    @Modifying
    @Query("delete from CreditOffer where bank_id = ?1")
    void deleteCreditOfferFromBank(UUID bankId);

}
