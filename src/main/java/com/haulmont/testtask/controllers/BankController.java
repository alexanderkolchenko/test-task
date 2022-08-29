package com.haulmont.testtask.controllers;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.CreditPayment;
import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.service.BankService;
import com.haulmont.testtask.service.CreditOfferService;
import com.haulmont.testtask.service.CreditPaymentService;
import com.haulmont.testtask.service.CreditService;
import com.haulmont.testtask.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Controller
public class BankController {

    @Autowired
    private BankService bankService;

    @Autowired
    private CreditService creditService;

    @Autowired
    private CreditOfferService creditOfferService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CreditPaymentService creditPaymentService;


    @GetMapping("/")
    public String getListOfBanks(Model model) {
        List<Bank> banks = bankService.getAllBanks();
        model.addAttribute("banks", banks);
        model.addAttribute("title", "Банки");
        return "index";
    }

    @GetMapping("/banks/add")
    public String getAddBankPage(Model model) {
        model.addAttribute("title", "Добавить банк");
        List<Credit> credits = creditService.getAllCredits();
        model.addAttribute("credits", credits);
        return "banks/banks_add";
    }

    @PostMapping("/banks/add")
    public String addBank(@RequestParam(required = false) String[] creditsId,
                          @RequestParam String nameOfBank) {
        Bank bank = new Bank(nameOfBank);
        bankService.saveBank(bank, creditsId);
        return "redirect:/";
    }

    @GetMapping("/banks/{id}")
    public String showBankDetails(@PathVariable(value = "id") UUID id, Model model) {
        Bank bank = bankService.getBank(id);
        if (bank == null) {
            //todo check wrong bank
        } else {

            //todo try add only bank with eager
            model.addAttribute("credits", bank.getCredits());
            model.addAttribute("bank", bank);
            model.addAttribute("customers", bank.getCustomers());
            model.addAttribute("creditOffers", bank.getCreditOffers());
            model.addAttribute("title", bank.getNameOfBank());
        }
        return "banks/banks_details";
    }

    @GetMapping("/banks_schedule/{bank_id}/{credit_offer_id}/{customer_id}")
    public String printPaymentSchedule(@PathVariable(value = "bank_id") UUID bankId, @PathVariable(value = "credit_offer_id") UUID creditOfferId,
                                       @PathVariable(value = "customer_id") UUID customer_id, Model model) {
        Bank bank = bankService.getBank(bankId);
        List<CreditPayment> creditPayments = creditPaymentService.getCreditPaymentsList(creditOfferId);
        Customer customer = customerService.getCustomer(customer_id);

        if (creditPayments != null && creditPayments.size() > 1) {
            creditPayments.sort(Comparator.comparing(CreditPayment::getDateOfPayment));
        }

        model.addAttribute("credits", bank.getCredits());
        model.addAttribute("bank", bank);
        model.addAttribute("customers", bank.getCustomers());
        model.addAttribute("creditOffers", bank.getCreditOffers());
        model.addAttribute("creditPayments", creditPayments);
        model.addAttribute("customer", customer);
        model.addAttribute("title", bank.getNameOfBank());
        return "banks/banks_schedule";
    }

    @GetMapping("/banks/edit/{id}")
    public String editBank(@PathVariable(value = "id") UUID id, Model model) {
        Bank bank = bankService.getBank(id);
        List<Credit> credits = creditService.getAllCredits();
        //todo try add only bank with eager
        model.addAttribute("credits", credits);
        model.addAttribute("creditsOfBank", bank.getCredits());
        model.addAttribute("creditOffers", bank.getCreditOffers());
        model.addAttribute("bank", bank);
        model.addAttribute("customers", bank.getCustomers());
        model.addAttribute("title", "Редактирование банка: " + bank.getNameOfBank());
        return "/banks/banks_edit";
    }

    @PostMapping("/banks/edit/{id}/{bank_id}/remove_credit_offer")
    public String deleteCreditOffersFromBank(@PathVariable(value = "id") UUID id, @PathVariable(value = "bank_id") UUID bank_id, Model model) {
        creditOfferService.deleteCreditOffers(id);
        //todo return to edit page
        return editBank(bank_id, model);
    }

    @PostMapping("/banks/edit/{id}")
    public String updateBank(@PathVariable(value = "id", required = false) UUID id,
                             @RequestParam(required = false) String[] creditsId,
                             @RequestParam String nameOfBank, Model model) {

        Bank bank = bankService.getBank(id);
        if (!bank.getNameOfBank().equals(nameOfBank)) {
            bank.setNameOfBank(nameOfBank);
        }
        bankService.updateBank(bank, creditsId);
        return editBank(id, model);
    }

    @PostMapping("/banks/remove/{id}")
    public String deleteBank(@PathVariable(value = "id") UUID id) {
        bankService.removeBank(id);
        return "redirect:/";
    }
}

