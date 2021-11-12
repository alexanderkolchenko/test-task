package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;
    private int creditLimit;
    private double interestRate;

    @ManyToMany(fetch = FetchType.LAZY/* cascade = CascadeType.ALL*/)
    @JoinTable(name = "credit_banks",
            joinColumns = {@JoinColumn(name = "credit_id")},
            inverseJoinColumns = {@JoinColumn(name = "bank_id")})
    private Set<Bank> banks = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "id")
    List<CreditOffer> creditOffers = new ArrayList<>();

    public Credit(int creditLimit, double interestRate) {
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public Credit() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public Set<Bank> getBanks() {
        return banks;
    }

    public void setBanks(Set<Bank> banks) {
        this.banks = banks;
    }
}
