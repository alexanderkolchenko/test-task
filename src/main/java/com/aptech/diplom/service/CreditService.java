package com.aptech.diplom.service;

import com.aptech.diplom.exception_handler.exception.NoSuchCreditException;
import com.aptech.diplom.models.Credit;
import com.aptech.diplom.repository.CreditRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreditService {

    private final CreditRepository creditRepository;

    public CreditService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    public Credit getCredit(UUID uuid) {
        return creditRepository.findById(uuid).orElseThrow(() -> new NoSuchCreditException("There is no credit with id = " + uuid));
    }

    public Credit addCredit(Credit credit) {
        return creditRepository.save(credit);
    }

    public Credit updateCredit(Credit credit, float interestRate, int creditLimit) {
        credit.setCreditLimit(creditLimit);
        credit.setInterestRate(interestRate);
        return creditRepository.save(credit);
    }

    public Credit deleteCredit(UUID uuid) {
        Credit credit = getCredit(uuid);
        creditRepository.delete(credit);
        return credit;
    }
}
