package com.haulmont.testtask.controllers;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.service.BankService;
import com.haulmont.testtask.service.CreditOfferService;
import com.haulmont.testtask.service.CreditService;
import com.haulmont.testtask.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
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


        List<Bank> banks = bankService.getAllBanks();
        List<Customer> customers = customerService.getAllCustomers();

        //todo what is it
        /*формирует список кредитов кождого банка отображения радио кнопок в аккордионе*/
     /*   bankService.getAllBanks().forEach(x -> {
            if (!x.getCredits().isEmpty()) {
                banks.add(x);
            }
        });     */
        model.addAttribute("banks", banks);
        model.addAttribute("clients", customers);
        model.addAttribute("title", "Кредитное предложение");
        return "credit_offers/credit_offers";
    }

    @PostMapping("/creditOffers")
    public String applyCreditOffer(@RequestParam String clientId,
                                   @RequestParam(required = true) float amountOfCredit,
                                   @RequestParam String creditBankId,
                                   @RequestParam String startDate,
                                   @RequestParam int numberOfMonth,
                                   @RequestParam(required = false) float interestRate) {
        //todo try to use modelattribute
        String[] ids = creditBankId.split(":");
        UUID customerId = UUID.fromString(clientId);
        UUID creditId = UUID.fromString(ids[0]);
        UUID bankId = UUID.fromString(ids[1]);

        Bank bank = bankService.getBank(bankId);
        Credit credit = creditService.getCredit(creditId);
        Customer customer = customerService.getCustomer(customerId);
        creditOfferService.applyCreditOffer(customer, credit, bank, amountOfCredit, interestRate, startDate, numberOfMonth);
        return "redirect:/creditOffers";
    }
}
