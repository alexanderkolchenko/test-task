package com.haulmont.testtask.repository;

import com.haulmont.testtask.models.CreditPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * интерфейс реализует взаимодействие объектов CreditPayment с базой данных
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */
public interface CreditPaymentRepository extends JpaRepository<CreditPayment, UUID> {

    /*используется для вывода графика платежей на странице "Информация о банке"*/
    @Query("from CreditPayment where co_cp_id = ?1")
    List<CreditPayment> findCreditPaymentsBycci(UUID co_cp_id);

    /*удаление платежей при удалении клиента и его кредитного предложения*/
    @Transactional
    @Modifying
    @Query("delete from CreditPayment where co_cp_id = ?1 ")
    void deleteCreditPaymentById(UUID id);
}
