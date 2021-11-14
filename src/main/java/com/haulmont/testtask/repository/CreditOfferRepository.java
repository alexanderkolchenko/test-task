package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.CreditOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * интерфейс реализует взаимодействие объектов CreditOffer с базой данных
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */

public interface CreditOfferRepository extends JpaRepository<CreditOffer, UUID> {

    /*удаление кредитных предложений при удалении клиента*/
    @Transactional
    @Modifying
    @Query("delete from CreditOffer where customer_id = ?1")
    void deleteCreditOfferByCustomer(UUID customerId);

    /*удаление кредитных предложений при удалении кредита*/
    @Transactional
    @Modifying
    @Query("delete from CreditOffer where credit_id = ?1")
    void deleteCreditOfferByCredit(UUID creditId);
}
