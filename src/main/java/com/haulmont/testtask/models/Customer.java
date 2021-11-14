package com.haulmont.testtask.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * Класс Клиент, содержит ФИО клиента, номер телефона, e-mail,
 * номер паспорта, список банков, в которых имеет кредиты и список кредитов
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;
    private String surname;
    private String name;
    private String patronymic;
    private String phoneNumber;
    private String email;
    private String passportNumber;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "customer_banks",
            joinColumns = {@JoinColumn(name = "customer_id")},
            inverseJoinColumns = {@JoinColumn(name = "bank_id")})
    private Set<Bank> listOfBanks = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    List<CreditOffer> creditOffers = new ArrayList<>();

    public Customer() {
    }

    public Customer(String surname, String name, String patronymic, String phoneNumber, String email, String passportNumber) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passportNumber = passportNumber;
    }

    /* Удаление банка из списка банков клиента при удалении объекта банка из БД */
    public void deleteBankFromCustomer(Bank bank) {
        this.listOfBanks.remove(bank);
        bank.getListOfCustomers().remove(this);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public Set<Bank> getBanks() {
        return listOfBanks;
    }

    public void setBanks(Set<Bank> banks) {
        this.listOfBanks = banks;
    }

    public List<CreditOffer> getCreditOffers() {
        return creditOffers;
    }

    public void setCreditOffers(List<CreditOffer> creditOffers) {
        this.creditOffers = creditOffers;
    }

    @Override
    public String toString() {
        return getSurname() + " " + getName() + " " + getPatronymic();
    }
}
