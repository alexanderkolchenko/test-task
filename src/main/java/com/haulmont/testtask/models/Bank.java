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
@Table(name = "banks")
public class Bank {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name_of_bank")
    private String nameOfBank;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "credit_banks",
            joinColumns = @JoinColumn(name = "bank_id"),
            inverseJoinColumns = @JoinColumn(name = "credit_id")
    )
    private List<Credit> credits;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "customer_banks",
            joinColumns = @JoinColumn(name = "bank_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<Customer> customers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bank")
    private List<CreditOffer> creditOffers;

    public Bank() {
    }

    public Bank(String nameOfBank) {
        this.nameOfBank = nameOfBank;
    }

    public void addCredit(Credit credit) {
        if (credits == null) {
            credits = new ArrayList<>();
        }
        credits.add(credit);
    }

    public void removeCredit(Credit credit) {
        credits.remove(credit);
    }

    public void addCustomer(Customer customer) {
        if (customers == null) {
            customers = new ArrayList<>();
        }
        customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        if (customers != null) {
            customer.removeBank(this);
            customers.remove(customer);
        }
    }

    public void removeCustomers() {
        for (Customer customer : customers) {
            customer.removeBank(this);
        }
        customers.clear();
    }

    public void removeCredits() {
        for (Credit credit : credits) {
            credit.removeBank(this);
        }
        credits.clear();
    }

    public List<Credit> getCredits() {
        if (credits == null) {
            credits = new ArrayList<>();
        }
        return credits;
    }

    public void setCredits(List<Credit> credits) {
        this.credits = credits;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

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

    public List<CreditOffer> getCreditOffers() {
        return creditOffers;
    }

    public void setCreditOffers(List<CreditOffer> creditOffers) {
        this.creditOffers = creditOffers;
    }

}
