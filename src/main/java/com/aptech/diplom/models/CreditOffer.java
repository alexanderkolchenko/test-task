package com.aptech.diplom.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "creditoffers")
public class CreditOffer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "credit_amount")
    private double creditAmount;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creditOffer")
    private List<CreditPayment> paymentSchedule;

    public CreditOffer() {
    }

    public CreditOffer(Customer customer, Credit credit, Bank bank, List<CreditPayment> paymentSchedule, double creditAmount) {
        this.customer = customer;
        this.credit = credit;
        this.bank = bank;
        this.paymentSchedule = paymentSchedule;
        for (CreditPayment payment : paymentSchedule) {
            payment.setCreditOffer(this);
        }
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

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public List<CreditPayment> getPaymentSchedule() {
        return paymentSchedule;
    }

    public void setPaymentSchedule(List<CreditPayment> paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }
}
