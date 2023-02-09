package com.aptech.diplom.controllers;

import com.aptech.diplom.models.Bank;
import com.aptech.diplom.models.Credit;
import com.aptech.diplom.models.Customer;
import com.aptech.diplom.service.BankService;
import com.aptech.diplom.service.CreditOfferService;
import com.aptech.diplom.service.CreditService;
import com.aptech.diplom.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class CreditOffersController {

    @Autowired
    private BankService bankService;

    @Autowired
    private CreditService creditService;

    @Autowired
    private CreditOfferService creditOfferService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/creditOffers")
    public String getCreditOffersPage(Model model) {
        model.addAttribute("banks", bankService.getAllBanks());
        model.addAttribute("clients", customerService.getAllCustomers());
        model.addAttribute("title", "Кредитное предложение");
        return "credit_offers/credit_offers";
    }

    @PostMapping("/creditOffers")
    public String applyCreditOffer(@RequestParam String clientId,
                                   @RequestParam float amountOfCredit,
                                   @RequestParam String creditBankId,
                                   @RequestParam String startDate,
                                   @RequestParam int numberOfMonth,
                                   @RequestParam(required = false) float interestRate) {
        String[] ids = creditBankId.split(":");
        UUID customerId = UUID.fromString(clientId);
        UUID creditId = UUID.fromString(ids[0]);
        UUID bankId = UUID.fromString(ids[1]);

        Bank bank = bankService.getBank(bankId);
        Credit credit = creditService.getCredit(creditId);
        Customer customer = customerService.getCustomer(customerId);
        creditOfferService.applyCreditOffer(customer, credit, bank, amountOfCredit, interestRate, startDate, numberOfMonth);
        return "redirect:/banks/" + bankId;
    }
}
