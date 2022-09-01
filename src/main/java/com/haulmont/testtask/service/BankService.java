package com.haulmont.testtask.service;

import com.haulmont.testtask.exception_handler.exception.NoSuchBankException;
import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CreditService creditService;

    private List<Credit> addedCredits;

    private List<Credit> removedCredits;

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Bank addBank(Bank bank, String[] creditsId) {
        /* Обрабатывает массив UUID кредитов, которые пришли с чекбокса списка кредитов */
        if (creditsId != null) {
            for (String id : creditsId) {
                UUID uuid = UUID.fromString(id);
                Credit c = creditService.getCredit(uuid);
                bank.addCredit(c);
            }
        }
        return bankRepository.save(bank);
    }

    public Bank getBank(UUID id) {
        return bankRepository.findById(id).orElseThrow(() -> new NoSuchBankException("There is no bank with id = " + id));
    }

    public Bank updateBank(Bank bank, String[] creditsId, String nameOfBank) {

        addedCredits = new ArrayList<>();
        removedCredits = new ArrayList<>();

        if (!bank.getNameOfBank().equals(nameOfBank)) {
            bank.setNameOfBank(nameOfBank);
        }

        List<Credit> oldCredits = new ArrayList<>(bank.getCredits());
        List<Credit> newCredits = new ArrayList<>();

        /* добавляем кредит после настройки чекбокса, если кредит отсутствовал в списке банка*/
        if (creditsId != null) {
            for (String id : creditsId) {
                UUID uuid = UUID.fromString(id);
                Credit credit = creditService.getCredit(uuid);
                newCredits.add(credit);
                if (!oldCredits.contains(credit)) {
                    //credit.addBank(bank);
                    bank.addCredit(credit);
                    addedCredits.add(credit);
                }
            }
        }
        /* удаляем кредит после настройки чекбокса, если кредит присутствовал в списке банка */
        for (Credit credit : oldCredits) {
            if (!newCredits.contains(credit)) {
                credit.removeBank(bank);
                bank.removeCredit(credit);
                removedCredits.add(credit);
            }
        }
        return bankRepository.save(bank);
    }

    public Bank deleteBank(UUID id) {
        Bank bank = getBank(id);
        bank.removeCustomers();
        bank.removeCredits();
        bankRepository.delete(bank);
        return bank;
    }

    public List<Credit> getAddedCredits() {
        return addedCredits;
    }

    public void setAddedCredits(List<Credit> addedCredits) {
        this.addedCredits = addedCredits;
    }

    public List<Credit> getRemovedCredits() {
        return removedCredits;
    }

    public void setRemovedCredits(List<Credit> removedCredits) {
        this.removedCredits = removedCredits;
    }
}
