package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "credit_limit")
    private int creditLimit;

    @Column(name = "interest_rate")
    private float interestRate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "credit_banks",
            joinColumns = {@JoinColumn(name = "credit_id")},
            inverseJoinColumns = {@JoinColumn(name = "bank_id")})
    private List<Bank> banks;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "credit")
    List<CreditOffer> creditOffers;

    public Credit() {
    }

    public Credit(int creditLimit, float interestRate) {
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    /* Удаление банка из списка банков при удалении объекта банка из БД */
   /* public void deleteBankFromCredit(Bank bank) {
        this.listOfBanks.remove(bank);
        bank.getListOfCredits().remove(this);
   }
   */

    public void addBank(Bank bank) {
        if (banks == null) {
            banks = new ArrayList<>();
        }
        banks.add(bank);
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public float getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

}
