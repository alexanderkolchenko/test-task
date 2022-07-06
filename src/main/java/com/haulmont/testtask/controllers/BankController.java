package com.haulmont.testtask.controllers;

import com.haulmont.testtask.models.*;
import com.haulmont.testtask.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Класс контролер, реализует добавление, редактирование и удаление банка
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */
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

    /*начальная страница со списком банков*/
    @GetMapping("/")
    public String getListOfBanks(Model model) {
        List<Bank> banks = new ArrayList<>();
        bankRepository.findAll().forEach(banks::add);
        model.addAttribute("banks", banks);
        model.addAttribute("title", "Банки");
        return "index";
    }

    /*страница добаления банка*/
    @GetMapping("/banks/add")
    public String getAddBankPage(Model model) {
        model.addAttribute("title", "Добавить банк");
        List<Credit> credits = new ArrayList<>();
        creditRepository.findAll().forEach(credits::add);
        model.addAttribute("credits", credits);
        return "banks/banks_add";
    }

    /*добаление банка*/
    @PostMapping("/banks/add")
    public String addBank(@PathVariable(value = "id", required = false) UUID id,
                          @RequestParam(required = false) String[] idOfCredit,
                          @RequestParam String nameOfBank, Model model) {
        Bank bank = new Bank(nameOfBank);

        /* Обрабатывает массив UUID кредитов, которые пришли с чекбокса списка кредитов */
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

    /*страница "Информация о банке"*/
    @GetMapping("/banks/{id}")
    public String getBankDetails(@PathVariable(value = "id") UUID id, Model model) {
        if (!bankRepository.existsById(id)) {
            return "redirect:/";
        }
        Bank bank = bankRepository.findById(id).orElse(new Bank());
        List<Credit> credits = new ArrayList<>(bank.getListOfCredits());
        List<Customer> customers = new ArrayList<>(bank.getListOfCustomers());
        List<CreditOffer> creditOffers = new ArrayList<>(bank.getCreditOffers());
        model.addAttribute("credits", credits);
        model.addAttribute("bank", bank);
        model.addAttribute("customers", customers);
        model.addAttribute("creditOffers", creditOffers);
        model.addAttribute("title", bank.getNameOfBank());
        return "banks/banks_details";
    }

    /*просмотр графика платежей по каждому клиенту*/
    @GetMapping("/banks_schedule/{id}/{co_id}/{customer_id}")
    public String printPaymentSchedule(@PathVariable(value = "id") UUID id, @PathVariable(value = "co_id") UUID co_id,
                                       @PathVariable(value = "customer_id") UUID customer_id, Model model) {
        Bank bank = bankRepository.findById(id).orElse(new Bank());
        List<Credit> credits = new ArrayList<>(bank.getListOfCredits());
        List<Customer> customers = new ArrayList<>(bank.getListOfCustomers());
        List<CreditOffer> creditOffers = new ArrayList<>(bank.getCreditOffers());

        /**/
        List<CreditPayment> creditPayments = new ArrayList<>();
        creditPaymentRepository.findCreditPaymentsBycci(co_id).forEach(creditPayments::add);
        Customer customer = customerRepository.findById(customer_id).orElseThrow(NoSuchElementException::new);
        Collections.sort(creditPayments, Comparator.comparing(CreditPayment::getDateOfPayment));

        model.addAttribute("credits", credits);
        model.addAttribute("bank", bank);
        model.addAttribute("customers", customers);
        model.addAttribute("creditOffers", creditOffers);
        model.addAttribute("creditPayments", creditPayments);
        model.addAttribute("customer", customer);
        model.addAttribute("title", bank.getNameOfBank());
        return "banks/banks_schedule";
    }

    /*страница редактирования банка*/
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
        return "/banks/banks_edit";
    }

    /*удаление оформленного кредита вместе с графиками со страницы редактирования банка*/
    @PostMapping("/banks/edit/{id}/{bank_id}/remove_credit_offer")
    public String deleteCreditOffersFromBank(@PathVariable(value = "id") UUID id, @PathVariable(value = "bank_id") UUID bank_id, Model model) {
        CreditOffer creditOffer = creditOfferRepository.findById(id).orElseThrow(NoSuchElementException::new);
        creditOfferRepository.delete(creditOffer);
        return editBank(bank_id, model);
    }

    /*редактирование банка, изменение названия и списка доступных кредитов*/
    @PostMapping("/banks/edit/{id}")
    public String updateBank(@PathVariable(value = "id", required = false) UUID id,
                             @RequestParam(required = false) String[] idOfCredit,
                             @RequestParam String nameOfBank, Model model) {
        Bank bank = bankRepository.findById(id).orElseThrow(NoSuchElementException::new);
        bank.setNameOfBank(nameOfBank);
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
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
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

