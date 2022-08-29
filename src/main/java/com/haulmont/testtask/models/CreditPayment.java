package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "credit_payments")
public class CreditPayment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "date_of_payment")
    private LocalDate dateOfPayment;

    @Column(name = "payment_of_month")
    private float paymentOfMonth;

    @Column(name = "payment_of_percent_in_month")
    private float paymentOfPercentInMonth;

    @Column(name = "payment_of_loan_body_in_month")
    private float paymentOfLoanBodyInMonth;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "credit_offer_id")
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

    public float getPaymentOfMonth() {
        return paymentOfMonth;
    }

    public float getPaymentOfPercentInMonth() {
        return paymentOfPercentInMonth;
    }

    public float getPaymentOfLoanBodyInMonth() {
        return paymentOfLoanBodyInMonth;
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
