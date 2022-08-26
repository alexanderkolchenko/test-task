package com.haulmont.testtask.controllers;

import com.haulmont.testtask.exception_handler.NoSuchBankException;
import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.CreditOffer;
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

import java.util.*;

@Controller
public class BankController {

    @Autowired
    BankService bankService;

    @Autowired
    CreditService creditService;

    @Autowired
    CreditOfferService creditOfferService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CreditPaymentService creditPaymentService;


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
    public String addBank(@RequestParam(required = false) String[] credits,
                          @RequestParam String nameOfBank) {
        Bank bank = new Bank(nameOfBank);
        bankService.saveBank(bank, credits);
        return "redirect:/";
    }

    @GetMapping("/banks/{id}")
    public String showBankDetails(@PathVariable(value = "id") UUID id, Model model) {
        Bank bank = bankService.getBankById(id);
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
        Bank bank = bankService.getBankById(bankId);
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
        Bank bank = bankService.getBankById(id);
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
                             @RequestParam(required = false) String[] idOfCredit,
                             @RequestParam String nameOfBank, Model model) {

        Bank bank = bankService.getBankById(id);

        if (!bank.getNameOfBank().equals(nameOfBank)) {
            bank.setNameOfBank(nameOfBank);
        }

        List<Credit> listOfCreditsOfBank = new ArrayList<>(bank.getListOfCredits());
        List<Credit> newCredit = new ArrayList<>();

        /* добавляем кредит после настройки чекбокса, если кредит отсутствовал в списке банка*/
        if (idOfCredit != null) {
            for (String i : idOfCredit) {
                UUID j = UUID.fromString(i);
                Credit c = creditRepository.findById(j).orElseThrow(NoSuchElementException::new);
                newCredit.add(c);
                if (!listOfCreditsOfBank.contains(c)) {
                    c.getBanks().add(bank);
                    bank.getListOfCredits().add(c);
                }
            }
        }

        /* удаляем кредит после настройки чекбокса, если кредит присутствовал в списке банка */
        for (Credit c : listOfCreditsOfBank) {
            if (!newCredit.contains(c)) {
                c.getBanks().remove(bank);
                bank.getListOfCredits().remove(c);
            }
        }
        bankRepository.save(bank);
        return editBank(id, model);
    }

    /*
     * Удаление банка и его связей с клиентами к кредитами,
     * удаляются все выданные банком кредитные предложения, их графики,
     * клиенты и кредиты остаются доступными для других банков
     */
    @PostMapping("/banks/remove/{id}")
    public String deleteBank(@PathVariable(value = "id") UUID id, Model model) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new NoSuchBankException("There is no bank with id = " + id));
        for (CreditOffer co : bank.getCreditOffers()) {
            creditPaymentRepository.deleteCreditPaymentById(co.getId());
        }
        customerRepository.findAll().forEach((c) -> c.deleteBankFromCustomer(bank));
        creditRepository.findAll().forEach((c) -> c.deleteBankFromCredit(bank));
        bank.getListOfCredits().clear();
        bank.getListOfCustomers().clear();
        bankRepository.delete(bank);
        return "redirect:/";
    }
}

