package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

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
    private float payPercentInMonth;
    private float payLoanBodyInMonth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="co_cp_id")
    private CreditOffer creditOffer;

    public CreditPayment() {
    }
    public CreditPayment(LocalDate dateOfPayment, float paymentOfMonth, float payPercentInMonth, float payLoanBodyInMonth) {
        this.dateOfPayment = dateOfPayment;
        this.paymentOfMonth = paymentOfMonth;
        this.payPercentInMonth = payPercentInMonth;
        this.payLoanBodyInMonth = payLoanBodyInMonth;
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

    public float getPayPercentInMonth() {
        return payPercentInMonth;
    }

    public void setPayPercentInMonth(float payPercentInMonth) {
        this.payPercentInMonth = payPercentInMonth;
    }

    public float getPayLoanBodyInMonth() {
        return payLoanBodyInMonth;
    }

    public void setPayLoanBodyInMonth(float payLoanBodyInMonth) {
        this.payLoanBodyInMonth = payLoanBodyInMonth;
    }

    @Override
    public String toString() {
        return "CreditPayment{" +
                "dateOfPayment=" + dateOfPayment +
                ", paymentOfMonth=" + paymentOfMonth +
                ", payPercentInMonth=" + payPercentInMonth +
                ", payLoanBodyInMonth=" + payLoanBodyInMonth +
                '}';
    }
}
