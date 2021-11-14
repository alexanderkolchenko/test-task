package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Класс Платеж по кредиту, содержит дату платежа, сумму платежа,
 * сумму гашения тела кредита, сумму гашения процентов и
 * кредитное предложение, к которому относится платеж
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */
@Entity
@Table(name = "credit_payments")
public class CreditPayment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    private LocalDate dateOfPayment;
    private float paymentOfMonth;
    private float paymentOfPercentInMonth;
    private float paymentOfLoanBodyInMonth;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name ="co_cp_id")
    private CreditOffer creditOffer;

    public CreditPayment() {
    }
    public CreditPayment(LocalDate dateOfPayment, float paymentOfMonth, float payPercentInMonth, float payLoanBodyInMonth) {
        this.dateOfPayment = dateOfPayment;
        this.paymentOfMonth = paymentOfMonth;
        this.paymentOfPercentInMonth = payPercentInMonth;
        this.paymentOfLoanBodyInMonth = payLoanBodyInMonth;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(LocalDate dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public float getPaymentOfMonth() {
        return paymentOfMonth;
    }

    public void setPaymentOfMonth(float paymentOfMonth) {
        this.paymentOfMonth = paymentOfMonth;
    }

    public float getPaymentOfPercentInMonth() {
        return paymentOfPercentInMonth;
    }

    public void setPaymentOfPercentInMonth(float paymentOfPercentInMonth) {
        this.paymentOfPercentInMonth = paymentOfPercentInMonth;
    }

    public float getPaymentOfLoanBodyInMonth() {
        return paymentOfLoanBodyInMonth;
    }

    public void setPaymentOfLoanBodyInMonth(float paymentOfLoanBodyInMonth) {
        this.paymentOfLoanBodyInMonth = paymentOfLoanBodyInMonth;
    }

    public CreditOffer getCreditOffer() {
        return creditOffer;
    }

    public void setCreditOffer(CreditOffer creditOffer) {
        this.creditOffer = creditOffer;
    }

    @Override
    public String toString() {
        return "CreditPayment{" +
                "dateOfPayment=" + dateOfPayment +
                ", paymentOfMonth=" + paymentOfMonth +
                ", payPercentInMonth=" + paymentOfPercentInMonth +
                ", payLoanBodyInMonth=" + paymentOfLoanBodyInMonth +
                '}';
    }
}
