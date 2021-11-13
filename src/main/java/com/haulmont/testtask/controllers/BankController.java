package com.haulmont.testtask.controllers;

import com.haulmont.testtask.models.*;
import com.haulmont.testtask.repository.BankRepository;
import com.haulmont.testtask.repository.CreditPaymentRepository;
import com.haulmont.testtask.repository.CreditRepository;
import com.haulmont.testtask.repository.CustomerRepository;
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


    //отсюда


    @GetMapping("/banks/edit/{id}")
    public String editBank(@PathVariable(value = "id") UUID id, Model model) {
        Bank bank = bankRepository.findById(id).orElse(new Bank());
        List<Credit> creditsOfBank = new ArrayList<>(bank.getListOfCredits());
        List<Credit> credits = new ArrayList<>();
        creditRepository.findAll().forEach(credits::add);
        model.addAttribute("credits", credits);
        model.addAttribute("creditsOfBank", creditsOfBank);
        model.addAttribute("bank", bank);
        model.addAttribute("title", "Редактирование банка: " + bank.getNameOfBank());
        return "banks/banks_edit";
    }


    @PostMapping("/banks/{id}")
    public String updateBank(@PathVariable(value = "id") UUID id, @RequestParam int bankLimit, @RequestParam double interestRate, Model model) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        /*bank.setCreditLimit(bankLimit);
        bank.setInterestRate(interestRate);*/

        bankRepository.save(bank);
        return "redirect:/";
    }

    @PostMapping("/banks/{id}/remove")
    public String deleteBank(@PathVariable(value = "id") UUID id, Model model) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        bankRepository.delete(bank);
        return "redirect:/";
    }
}

