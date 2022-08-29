package com.haulmont.testtask.service;

import com.haulmont.testtask.exception_handler.NoSuchCreditOfferException;
import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.CreditOffer;
import com.haulmont.testtask.models.CreditPayment;
import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.repository.CreditOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CreditOfferService {

    @Autowired
    private CreditOfferRepository creditOffersRepository;

    public List<CreditOffer> getAllCreditOffers() {
        return creditOffersRepository.findAll();
    }

    public CreditOffer getCreditOfferById(UUID id) {
        return creditOffersRepository.findById(id).orElseThrow(() -> new NoSuchCreditOfferException("There is no credit offer with id = " + id));
    }

    public void deleteCreditOffers(UUID id) {
        creditOffersRepository.deleteById(id);
    }

    public void applyCreditOffer(Customer customer, Credit credit, Bank bank, float amountOfCredit, float interestRate, String startDate, int numberOfMonth) {
        float amountOfCreditConst = amountOfCredit;
        float payPercentInMonth;
        float payLoanBodyInMonth;
        float paymentOfMonth;
        float percentOfMonth;

        LocalDate currentDate = LocalDate.parse(startDate);
        List<CreditPayment> paymentSchedule = new ArrayList<>();

        percentOfMonth = interestRate / (100 * 12);
        percentOfMonth = withMathRound(percentOfMonth, 6);
        paymentOfMonth = (float) (amountOfCredit * (percentOfMonth / (1 - (Math.pow((percentOfMonth + 1), numberOfMonth * -1)))));
        paymentOfMonth = withMathRound(paymentOfMonth, 3);
        for (int i = 0; i < numberOfMonth; i++) {
            payPercentInMonth = (amountOfCredit * percentOfMonth);
            payPercentInMonth = withMathRound(payPercentInMonth, 3);
            payLoanBodyInMonth = (paymentOfMonth - payPercentInMonth);
            payLoanBodyInMonth = withMathRound(payLoanBodyInMonth, 3);
            currentDate = currentDate.plusMonths(1);
            CreditPayment creditPayment = new CreditPayment(currentDate, paymentOfMonth, payPercentInMonth, payLoanBodyInMonth);
            paymentSchedule.add(creditPayment);
            amountOfCredit -= payLoanBodyInMonth;
        }
        CreditOffer creditOffer = new CreditOffer(customer, credit, bank, paymentSchedule, amountOfCreditConst);
        bank.addCustomer(customer);
        customer.addBank(bank);
        creditOffersRepository.save(creditOffer);
    }

    /*округление типа float до нужного количества знаков после запятой*/
    public static float withMathRound(float value, int places) {
        double scale = Math.pow(10, places);
        return (float) (Math.round(value * scale) / scale);
    }
}
