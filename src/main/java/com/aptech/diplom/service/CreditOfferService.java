package com.aptech.diplom.service;

import com.aptech.diplom.models.Bank;
import com.aptech.diplom.repository.CreditOfferRepository;
import com.aptech.diplom.exception_handler.exception.NoSuchCreditOfferException;
import com.aptech.diplom.models.Credit;
import com.aptech.diplom.models.CreditOffer;
import com.aptech.diplom.models.CreditPayment;
import com.aptech.diplom.models.Customer;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CreditOfferService {

    private final CreditOfferRepository creditOffersRepository;

    public CreditOfferService(CreditOfferRepository creditOffersRepository) {
        this.creditOffersRepository = creditOffersRepository;
    }

    public List<CreditOffer> getAllCreditOffers() {
        return creditOffersRepository.findAll();
    }

    public CreditOffer getCreditOfferById(UUID id) {
        return creditOffersRepository.findById(id).orElseThrow(() -> new NoSuchCreditOfferException("There is no credit offer with id = " + id));
    }

    public CreditOffer deleteCreditOffers(UUID id) {
        CreditOffer creditOffer = getCreditOfferById(id);
        creditOffersRepository.delete(creditOffer);
        return creditOffer;
    }

    public CreditOffer create() {
        return new CreditOffer();
    }

    public CreditOffer applyCreditOffer(Customer customer, Credit credit, Bank bank,
                                        float amountOfCredit,
                                        float interestRate, String startDate,
                                        int numberOfMonth) {
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
        CreditOffer co = create();
        co.setCustomer(customer);
        co.setCredit(credit);
        co.setBank(bank);
        co.setPaymentSchedule(paymentSchedule);
        co.setCreditAmount(amountOfCreditConst);
        //CreditOffer creditOffer = new CreditOffer(customer, credit, bank, paymentSchedule, amountOfCreditConst);
        bank.addCustomer(customer);
        return creditOffersRepository.save(co);
    }

    public static float withMathRound(float value, int places) {
        double scale = Math.pow(10, places);
        return (float) (Math.round(value * scale) / scale);
    }
}
