package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * Класс Кредит, содержит кредитный лимит, процентную ставку,
 * спикок банков, в котором его можно оформить и список выданных
 * кредитных предложений
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */
@Entity
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    private int creditLimit;

    private float interestRate;

    /* Список банков, в которых можно оформить кредит */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "credit_banks",
            joinColumns = {@JoinColumn(name = "credit_id")},
            inverseJoinColumns = {@JoinColumn(name = "bank_id")})
    private Set<Bank> listOfBanks = new HashSet<>();

    /*Список выданных кредитных предложений по данному кредиту*/
    @OneToMany
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    List<CreditOffer> listOfCreditOffers = new ArrayList<>();

    public Credit() {
    }

    public Credit(int creditLimit, float interestRate) {
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    /* Удаление банка из списка банков при удалении объекта банка из БД */
    public void deleteBankFromCredit(Bank bank) {
        this.listOfBanks.remove(bank);
        bank.getListOfCredits().remove(this);
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

    public Set<Bank> getBanks() {
        return listOfBanks;
    }

    public void setBanks(Set<Bank> banks) {
        this.listOfBanks = banks;
    }

    public List<CreditOffer> getCreditOffers() {
        return listOfCreditOffers;
    }

    public void setCreditOffers(List<CreditOffer> creditOffers) {
        this.listOfCreditOffers = creditOffers;
    }
}
