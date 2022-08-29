package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.CreditPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface CreditPaymentRepository extends JpaRepository<CreditPayment, UUID> {

    List<CreditPayment> findAllByCreditOfferId(UUID Id);

    /*удаление платежей при удалении клиента и его кредитного предложения*/
    @Transactional
    @Modifying
    @Query("delete from CreditPayment where credit_offer_id = ?1")
    void deleteByCreditOfferId(UUID id);
}
