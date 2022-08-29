package com.haulmont.testtask.service;

import com.haulmont.testtask.exception_handler.NoSuchBankException;
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

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public void saveBank(Bank bank, String[] creditsId) {
        /* Обрабатывает массив UUID кредитов, которые пришли с чекбокса списка кредитов */
        if (creditsId != null) {
            for (String id : creditsId) {
                UUID uuid = UUID.fromString(id);
                Credit c = creditService.getCredit(uuid);
                bank.addCredit(c);
                c.addBank(bank);
            }
        }
        bankRepository.save(bank);
    }

    public Bank getBank(UUID id) {
        return bankRepository.findById(id).orElseThrow(() -> new NoSuchBankException("There is no bank with id = " + id));
    }

    public void updateBank(Bank bank, String[] creditsId) {
        List<Credit> oldCredits = new ArrayList<>(bank.getCredits());
        List<Credit> newCredits = new ArrayList<>();

        /* добавляем кредит после настройки чекбокса, если кредит отсутствовал в списке банка*/
        if (creditsId != null) {
            for (String id : creditsId) {
                UUID uuid = UUID.fromString(id);
                Credit credit = creditService.getCredit(uuid);
                newCredits.add(credit);
                if (!oldCredits.contains(credit)) {
                    credit.addBank(bank);
                    bank.addCredit(credit);
                }
            }
        }
        /* удаляем кредит после настройки чекбокса, если кредит присутствовал в списке банка */
        for (Credit credit : oldCredits) {
            if (!newCredits.contains(credit)) {
                credit.removeBank(bank);
                bank.removeCredit(credit);
            }
        }
        bankRepository.save(bank);
    }

    public void removeBank(UUID id) {
        Bank bank = getBank(id);
        bank.removeCustomers();
        bank.removeCredits();
        bankRepository.delete(bank);
    }
}
