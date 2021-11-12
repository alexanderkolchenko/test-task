package com.haulmont.testtask.controllers;


import com.haulmont.testtask.models.*;
import com.haulmont.testtask.repository.BankRepository;
import com.haulmont.testtask.repository.CreditOfferRepository;
import com.haulmont.testtask.repository.CreditRepository;
import com.haulmont.testtask.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
public class CreditOffersController {

    @Autowired
    CreditOfferRepository creditOfferRepository;

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CreditOfferRepository creditOffersRepository;


    @GetMapping("/creditOffers")
    public String getCredits(Model model) {
        List<Bank> banks = new ArrayList<>();
        List<Customer> clients = new ArrayList<>();
        bankRepository.findAll().forEach(x -> {
            if (!x.getListOfCredits().isEmpty()) {
                banks.add(x);
            }
        });
        customerRepository.findAll().forEach(clients::add);
        model.addAttribute("banks", banks);
        model.addAttribute("clients", clients);
        model.addAttribute("title", "Кредитное предложение");
        return "credit_offers/credit_offers";
    }



    @PostMapping("/creditOffers")
    public String addCreditOffer(@RequestParam String chooseClient,
                                 @RequestParam(required = true) float amountOfCredit,
                                 @RequestParam String loanSelection,
                                 @RequestParam String startDate,
                                 @RequestParam int numberOfMonth,
                                 @RequestParam(required = false) float interestRate,
                                 Model model) throws NoSuchElementException {

        float amountOfCreditConst = amountOfCredit;
        float payPercentInMonth;
        float payLoanBodyInMonth;
        float paymentOfMonth;
        float percentOfMonth;
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        String[] s = loanSelection.split(":");
        UUID idOfCustomer = UUID.fromString(chooseClient);
        UUID idOfCredit = UUID.fromString(s[0]);
        UUID idOfBank = UUID.fromString(s[1]);
        LocalDate currentDate = LocalDate.parse(startDate);
        List<CreditPayment> paymentSchedule = new ArrayList<>();

        percentOfMonth = interestRate / (100 * 12);
        percentOfMonth = withMathRound(percentOfMonth, 6);

        paymentOfMonth = (float) (amountOfCredit * (percentOfMonth / (1 - (Math.pow((percentOfMonth + 1), numberOfMonth * -1)))));
        paymentOfMonth = withMathRound(paymentOfMonth, 2);
        for (int i = 0; i < numberOfMonth; i++) {
            payPercentInMonth = (amountOfCredit * percentOfMonth);
            payPercentInMonth = withMathRound(payPercentInMonth, 2);
            payLoanBodyInMonth = (paymentOfMonth - payPercentInMonth);
            payLoanBodyInMonth = withMathRound(payLoanBodyInMonth, 2);
            currentDate = currentDate.plusMonths(1);
            CreditPayment creditPayment = new CreditPayment(currentDate, paymentOfMonth, payPercentInMonth, payLoanBodyInMonth);
            paymentSchedule.add(creditPayment);
            amountOfCredit -= payLoanBodyInMonth;
        }
        Bank bank = bankRepository.findById(idOfBank).orElseThrow(() -> new NoSuchElementException());
        Credit credit = creditRepository.findById(idOfCredit).orElseThrow(() -> new NoSuchElementException());
        Customer customer = customerRepository.findById(idOfCustomer).orElseThrow(() -> new NoSuchElementException());
        CreditOffer creditOffer = new CreditOffer(customer, credit, bank, paymentSchedule, amountOfCreditConst);
        bank.getListOfCustomers().add(customer);
        customer.getBanks().add(bank);

        creditOfferRepository.save(creditOffer);
        System.out.println("norm");
        return "redirect:/";
    }


    public static float withMathRound(float value, int places) {
        double scale = Math.pow(10, places);
        return (float) (Math.round(value * scale) / scale);
    }

}
