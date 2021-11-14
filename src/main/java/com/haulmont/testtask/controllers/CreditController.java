package com.haulmont.testtask.controllers;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.CreditOffer;
import com.haulmont.testtask.models.Customer;
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
    public String addCreditPage(Model model) {
        model.addAttribute("title", "Добавить кредит");
        return "credits/credits_add";
    }
    @PostMapping("/credits/add")
    //todo valid
    public String addCreditSubmit(@RequestParam int creditLimit, @RequestParam double interestRate, Model model) {
        Credit credit = new Credit(creditLimit, interestRate);
        creditRepository.save(credit);
        return "redirect:/credits";
    }


    @GetMapping("/credits/{id}")
    public String editCredit(@PathVariable(value = "id") UUID id, Model model) {
        System.out.println(id);
        if (!creditRepository.existsById(id)) {
            return "redirect:/";
        }
        Credit credit = creditRepository.findById(id).orElse(new Credit());
        model.addAttribute("credit", credit);
        model.addAttribute("title", "Редактирование кредита");
        return "credits/credits_edit";
    }

    @PostMapping("/credits/{id}")
    public String updateCredit(@PathVariable(value = "id") UUID id, @RequestParam int creditLimit, @RequestParam double interestRate, Model model) {
        Credit credit = creditRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        credit.setCreditLimit(creditLimit);
        credit.setInterestRate(interestRate);
        creditRepository.save(credit);
        return "redirect:/credits";
    }

    @PostMapping("/credits/{id}/remove")
    public String deleteСredit(@PathVariable(value = "id") UUID id, Model model) {
        Credit credit = creditRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        for (CreditOffer co : credit.getCreditOffers()) {
            System.out.println(co.getId());
            creditPaymentRepository.deleteCreditPaymentById(co.getId());
        }
        creditOfferRepository.deleteCreditOfferByCredit(id);
        bankRepository.findAll().forEach((b)->b.deleteCreditFromBank(credit));
        credit.getBanks().clear();
        creditRepository.delete(credit);
        //creditRepository.deleteCreditOffer(id);
        //creditRepository.deleteCredit(id);
        return "redirect:/credits";
    }
}
