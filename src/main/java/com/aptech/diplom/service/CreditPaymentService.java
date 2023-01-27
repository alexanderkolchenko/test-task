package com.aptech.diplom.service;

import com.aptech.diplom.repository.CreditPaymentRepository;
import com.aptech.diplom.models.CreditPayment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreditPaymentService {

    private final CreditPaymentRepository creditPaymentRepository;

    public CreditPaymentService(CreditPaymentRepository creditPaymentRepository) {
        this.creditPaymentRepository = creditPaymentRepository;
    }

    public List<CreditPayment> getCreditPaymentsList(UUID creditOfferId) {
        return creditPaymentRepository.findAllByCreditOfferId(creditOfferId);
    }
}
