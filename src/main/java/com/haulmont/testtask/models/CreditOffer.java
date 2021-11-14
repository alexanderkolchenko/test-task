package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Класс Кредитное предложение, содержит кредит, сумму кредита, клиента,
 * выдавший его банк и график платежей
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */
@Entity
@Table(name = "creditoffers")
public class CreditOffer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    private double creditAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "co_cp_id", referencedColumnName = "id")
    private List<CreditPayment> paymentSchedule;

    public CreditOffer() {
    }

    public CreditOffer(Customer customer, Credit credit, Bank bank, List<CreditPayment> paymentSchedule, double creditAmount) {
        this.customer = customer;
        this.credit = credit;
        this.bank = bank;
        this.paymentSchedule = paymentSchedule;
        this.creditAmount = creditAmount;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public List<CreditPayment> getPaymentSchedule() {
        return paymentSchedule;
    }

    public void setPaymentSchedule(List<CreditPayment> paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }
}
