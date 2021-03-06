package com.haulmont.testtask.controllers;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.CreditOffer;
import com.haulmont.testtask.repository.BankRepository;
import com.haulmont.testtask.repository.CreditOfferRepository;
import com.haulmont.testtask.repository.CreditPaymentRepository;
import com.haulmont.testtask.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


/**
 * Класс контролер, реализует добавление, редактирование и удаление кредита
 *
 * @author Alexander Kolchenko
 * @version 1.01 14.11.2021
 */
@Controller
public class CreditController {
    @Autowired
    CreditRepository creditRepository;

    @Autowired
    CreditOfferRepository creditOfferRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    CreditPaymentRepository creditPaymentRepository;

    @GetMapping("/credits")
    public String getCredits(Model model) {
        List<Credit> credits = new ArrayList<>();
        creditRepository.findAll().forEach(credits::add);
        model.addAttribute("credits", credits);
        model.addAttribute("title", "Кредиты");
        return "credits/credits";
    }

    @GetMapping("/credits/add")
    public String getCreditsPage(Model model) {
        model.addAttribute("title", "Добавить кредит");
        return "credits/credits_add";
    }

    @PostMapping("/credits/add")
    public String addCreditSubmit(@RequestParam int creditLimit, @RequestParam float interestRate, Model model) {
        interestRate = CreditOffersController.withMathRound(interestRate, 2);
        Credit credit = new Credit(creditLimit, interestRate);
        creditRepository.save(credit);
        return "redirect:/credits";
    }

    @GetMapping("/credits/{id}")
    public String editCredit(@PathVariable(value = "id") UUID id, Model model) {
        if (!creditRepository.existsById(id)) {
            return "redirect:/";
        }
        Credit credit = creditRepository.findById(id).orElse(new Credit());
        model.addAttribute("credit", credit);
        model.addAttribute("title", "Редактирование кредита");
        return "credits/credits_edit";
    }

    @PostMapping("/credits/{id}")
    public String updateCredit(@PathVariable(value = "id") UUID id, @RequestParam int creditLimit, @RequestParam float interestRate, Model model) {
        Credit credit = creditRepository.findById(id).orElseThrow(NoSuchElementException::new);
        credit.setCreditLimit(creditLimit);
        credit.setInterestRate(interestRate);
        creditRepository.save(credit);
        return "redirect:/credits";
    }

    /*
    * Удаление кредита и его связей с клиентами и банками,
    * удаляются все выданные кредитные предложения с этим кредитом, и их графики
    */
    @PostMapping("/credits/{id}/remove")
    public String deleteCredit(@PathVariable(value = "id") UUID id, Model model) {
        Credit credit = creditRepository.findById(id).orElseThrow(NoSuchElementException::new);
        for (CreditOffer co : credit.getCreditOffers()) {
            creditPaymentRepository.deleteCreditPaymentById(co.getId());
        }
        creditOfferRepository.deleteCreditOfferByCredit(id);
        bankRepository.findAll().forEach((b)->b.deleteCreditFromBank(credit));
        credit.getBanks().clear();
        creditRepository.delete(credit);
        return "redirect:/credits";
    }
}
