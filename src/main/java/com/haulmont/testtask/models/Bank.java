package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * Класс Банк, содержит список кредитов, список клиентов и
 * список выданных кредитных предложений
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */
@Entity
@Table(name = "banks")
public class Bank {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    private String nameOfBank;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "listOfBanks")
    private Set<Credit> listOfCredits = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "listOfBanks")
    private Set<Customer> listOfCustomers = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    List<CreditOffer> listOfCreditOffers = new ArrayList<>();

    public Bank(String nameOfBank) {
        this.nameOfBank = nameOfBank;
    }

    public Bank() {
    }

    /* Удаление кредита из списка кредитов банка при удалении объекта кредита из БД */
    public void deleteCreditFromBank(Credit credit) {
        this.listOfCredits.remove(credit);
        credit.getBanks().remove(this);
    }

    /* Удаление клиента из списка клиетов банка при удалении объекта клиента из БД */
    public void deleteCustomerFromBank(Customer customer) {
        this.listOfCustomers.remove(customer);
        customer.getBanks().remove(this);
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
        return listOfCreditOffers;
    }

    public void setCreditOffers(List<CreditOffer> creditOffers) {
        this.listOfCreditOffers = creditOffers;
    }
}
