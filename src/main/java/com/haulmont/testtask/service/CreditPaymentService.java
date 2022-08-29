package com.haulmont.testtask.service;

import com.haulmont.testtask.models.CreditPayment;
import com.haulmont.testtask.repository.CreditPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreditPaymentService {

    @Autowired
    private CreditPaymentRepository creditPaymentRepository;

    public List<CreditPayment> getCreditPaymentsList(UUID creditOfferId) {
        return creditPaymentRepository.findAllByCreditOfferId(creditOfferId);
    }
}
