package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "banks")
public class Bank {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    private String nameOfBank;

    @ManyToMany(fetch = FetchType.LAZY,/* cascade = CascadeType.ALL,*/ mappedBy = "banks")
    private Set<Credit> listOfCredits = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "banks")
    private Set<Customer> listOfCustomers = new HashSet<>();

    public Bank(String nameOfBank) {
        this.nameOfBank = nameOfBank;
    }

    public Bank() {
    }


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    List<CreditOffer> creditOffers = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNameOfBank() {
        return nameOfBank;
    }

    public void setNameOfBank(String nameOfBank) {
        this.nameOfBank = nameOfBank;
    }

    public Set<Credit> getListOfCredits() {
        return listOfCredits;
    }

    public void setListOfCredits(Set<Credit> listOfCredits) {
        this.listOfCredits = listOfCredits;
    }

    public Set<Customer> getListOfCustomers() {
        return listOfCustomers;
    }

    public void setListOfCustomers(Set<Customer> listOfCustomers) {
        this.listOfCustomers = listOfCustomers;
    }

    public List<CreditOffer> getCreditOffers() {
        return creditOffers;
    }

    public void setCreditOffers(List<CreditOffer> creditOffers) {
        this.creditOffers = creditOffers;
    }
}
