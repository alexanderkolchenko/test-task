package com.haulmont.testtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Веб-приложение реализует добавление, редактирование и удаление следующих
 * сущностей: банк, клиент, кредит, кредитное предложение, процесс оформления кредита
 * на клиента с созданием графика платежей и расчетом необходимых сумм,
 * автоматический расчет итоговой суммы процентов по кредиту,
 * автоматический расчет суммы ежемесячного платежа с учетом процентной ставки.
 *
 * * @author Alexander Kolchenko
 * * @version 1.01 14.11.2021
 */
@SpringBootApplication
public class TesttaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(TesttaskApplication.class, args);
    }
}
