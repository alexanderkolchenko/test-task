package com.haulmont.testtask.controllers;

import com.haulmont.testtask.models.*;
import com.haulmont.testtask.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class BankController {

    @Autowired
    BankRepository bankRepository;

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    CreditPaymentRepository creditPaymentRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CreditOfferRepository creditOfferRepository;


    @GetMapping("/")
    public String getBanks(Model model) {
        List<Bank> banks = new ArrayList<>();
        bankRepository.findAll().forEach(banks::add);
        model.addAttribute("banks", banks);
        model.addAttribute("title", "Банки");
        return "index";
    }

    @GetMapping("/banks/add")
    public String addBankPage(Model model) {
        model.addAttribute("title", "Добавить банк");
        List<Credit> credits = new ArrayList<>();
        creditRepository.findAll().forEach(credits::add);
        model.addAttribute("credits", credits);
        return "banks/banks_add";
    }


    @PostMapping("/banks/add")
    public String addBankSubmit(@PathVariable(value = "id", required = false) UUID id,
                                @RequestParam(required = false) String[] idOfCredit,
                                @RequestParam String nameOfBank, Model model) {
        Bank bank = new Bank(nameOfBank);
        if (idOfCredit != null) {
            for (String i : idOfCredit) {
                UUID j = UUID.fromString(i);
                Credit c = creditRepository.findById(j).orElseThrow(() -> new NoSuchElementException());
                c.getBanks().add(bank);
                bank.getListOfCredits().add(c);
            }
        }
        bankRepository.save(bank);
        return "redirect:/";
    }


    @GetMapping("/banks/{id}")
    public String getBankDetails(@PathVariable(value = "id") UUID id, Model model) {
        if (!bankRepository.existsById(id)) {
            return "redirect:/";
        }
        Bank bank = bankRepository.findById(id).orElse(new Bank());
        List<Credit> credits = new ArrayList<>(bank.getListOfCredits());
        List<Customer> customers = new ArrayList<>(bank.getListOfCustomers());
        List<CreditOffer> creditOffers = new ArrayList<>(bank.getCreditOffers());
        Map<CreditOffer, List<CreditPayment>> creditPayments = new LinkedHashMap<>();
        for (CreditOffer co : creditOffers) {
            List<CreditPayment> creditPayment = co.getPaymentSchedule();
            Collections.sort(creditPayment, (a, b) -> a.getDateOfPayment().compareTo(b.getDateOfPayment()));
            creditPayments.put(co, creditPayment);
        }

        List<Integer> index = new LinkedList<>();

        for (int i = 0; i < creditPayments.size(); i++) {
            index.add(i);
        }
        model.addAttribute("index", index);
        model.addAttribute("credits", credits);
        model.addAttribute("bank", bank);
        model.addAttribute("customers", customers);
        model.addAttribute("creditOffers", creditOffers);
        model.addAttribute("creditPayments", creditPayments);
        model.addAttribute("title", bank.getNameOfBank());
        return "banks/banks_details";
    }

    @GetMapping("/banks_schedule/{id}/{co_id}/{customer_id}")
    public String printPaymentSchedule(@PathVariable(value = "id") UUID id, @PathVariable(value = "co_id") UUID co_id,
                                       @PathVariable(value = "customer_id") UUID customer_id, Model model) {
        Bank bank = bankRepository.findById(id).orElse(new Bank());
        List<Credit> credits = new ArrayList<>(bank.getListOfCredits());
        List<Customer> customers = new ArrayList<>(bank.getListOfCustomers());
        List<CreditPayment> creditPayments = new ArrayList<>();
        creditPaymentRepository.findCreditPaymentsBycci(co_id).forEach(creditPayments::add);
        List<CreditOffer> creditOffers = new ArrayList<>(bank.getCreditOffers());
        Customer customer = customerRepository.findById(customer_id).orElseThrow(() -> new NoSuchElementException());
        Collections.sort(creditPayments, (a, b) -> a.getDateOfPayment().compareTo(b.getDateOfPayment()));
        model.addAttribute("credits", credits);
        model.addAttribute("bank", bank);
        model.addAttribute("customers", customers);
        model.addAttribute("creditOffers", creditOffers);
        model.addAttribute("creditPayments", creditPayments);
        model.addAttribute("customer", customer);
        //model.addAttribute("creditPayments", creditPayments);
        model.addAttribute("title", bank.getNameOfBank());
        //model.addAttribute("index", index);
        System.out.println(customers);
        return "banks/banks_schedule";
    }

    @GetMapping("/banks/edit/{id}")
    public String editBank(@PathVariable(value = "id") UUID id, Model model) {
        Bank bank = bankRepository.findById(id).orElse(new Bank());
        List<Credit> creditsOfBank = new ArrayList<>(bank.getListOfCredits());
        List<Credit> credits = new ArrayList<>();
        List<Customer> customers = new ArrayList<>(bank.getListOfCustomers());
        List<CreditOffer> creditOffers = new ArrayList<>(bank.getCreditOffers());
        creditRepository.findAll().forEach(credits::add);
        model.addAttribute("credits", credits);
        model.addAttribute("creditsOfBank", creditsOfBank);
        model.addAttribute("creditOffers", creditOffers);
        model.addAttribute("bank", bank);
        model.addAttribute("customers", customers);
        model.addAttribute("title", "Редактирование банка: " + bank.getNameOfBank());
        return "banks/banks_edit";
    }

    //удаление выданного кредита
    @PostMapping("/banks/edit/{id}/{bank_id}/remove_credit_offer")
    public String deleteCreditOffer(@PathVariable(value = "id") UUID id, @PathVariable(value = "bank_id") UUID bank_id, Model model) {
        CreditOffer creditOffer = creditOfferRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        creditOfferRepository.delete(creditOffer);
        return editBank(bank_id, model);
    }


    //редктирование банка
    @PostMapping("/banks/edit/{id}")
    public String editBank(@PathVariable(value = "id", required = false) UUID id,
                           @RequestParam(required = false) String[] idOfCredit,
                           @RequestParam String nameOfBank, Model model) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        bank.setNameOfBank(nameOfBank);
        List<Credit> listOfCreditsOfBank = new ArrayList<>(bank.getListOfCredits());
        //List<Credit> listOfAllCredit = creditRepository.findAll();
        List<Credit> newCredit = new ArrayList<>();

        if (idOfCredit != null) {
            for (String i : idOfCredit) {
                UUID j = UUID.fromString(i);
                Credit c = creditRepository.findById(j).orElseThrow(() -> new NoSuchElementException());
                newCredit.add(c);
                if (!listOfCreditsOfBank.contains(c)) {
                    c.getBanks().add(bank);
                    bank.getListOfCredits().add(c);
                }
            }
        }
        for(Credit c : listOfCreditsOfBank) {
            if(!newCredit.contains(c)) {
                c.getBanks().remove(bank);
                bank.getListOfCredits().remove(c);
            }
        }

        bankRepository.save(bank);
        return editBank(id, model);
    }

    //отсюда
    @PostMapping("/banks/{id}/remove")
    public String deleteBank(@PathVariable(value = "id") UUID id, Model model) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        bankRepository.delete(bank);
        return "redirect:/";
    }
}

