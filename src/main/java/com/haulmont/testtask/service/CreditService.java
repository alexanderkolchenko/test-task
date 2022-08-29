package com.haulmont.testtask.service;

import com.haulmont.testtask.exception_handler.exception.NoSuchCreditException;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreditService {

    @Autowired
    private CreditRepository creditRepository;


    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    public Credit getCredit(UUID uuid) {
        return creditRepository.findById(uuid).orElseThrow(() -> new NoSuchCreditException("There is no credit with id = " + uuid));
    }

    public void addCredit(Credit credit) {
        creditRepository.save(credit);
    }

    public void updateCredit(Credit credit) {
        creditRepository.save(credit);
    }

    public void removeCredit(UUID uuid) {
        Credit credit = getCredit(uuid);
        creditRepository.delete(credit);
    }
}
