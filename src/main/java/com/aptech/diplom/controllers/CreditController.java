package com.aptech.diplom.controllers;

import com.aptech.diplom.models.Credit;
import com.aptech.diplom.service.CreditOfferService;
import com.aptech.diplom.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;


@Controller
public class CreditController {
    @Autowired
    private CreditService creditService;

    @GetMapping("/credits")
    public String getCredits(Model model) {
        model.addAttribute("credits", creditService.getAllCredits());
        model.addAttribute("title", "Кредиты");
        return "credits/credits";
    }

    @GetMapping("/credits/add")
    @PreAuthorize("hasAuthority('SUPERUSER')")
    public String addCreditsPage(Model model) {
        model.addAttribute("title", "Добавить кредит");
        return "credits/credits_add";
    }

    @PostMapping("/credits/add")
    @PreAuthorize("hasAuthority('SUPERUSER')")
    public String addCredit(@RequestParam int creditLimit, @RequestParam float interestRate) {
        interestRate = CreditOfferService.withMathRound(interestRate, 2);
        Credit credit = new Credit(creditLimit, interestRate);
        creditService.addCredit(credit);
        return "redirect:/credits";
    }

    @GetMapping("/credits/{id}")
    @PreAuthorize("hasAuthority('SUPERUSER')")
    public String editCreditPage(@PathVariable(value = "id") UUID id, Model model) {
        Credit credit = creditService.getCredit(id);
        if (credit == null) {
            return "redirect:/";
        }
        model.addAttribute("credit", credit);
        model.addAttribute("title", "Редактирование кредита");
        return "credits/credits_edit";
    }

    @PostMapping("/credits/{id}")
    @PreAuthorize("hasAuthority('SUPERUSER')")
    public String updateCredit(@PathVariable(value = "id") UUID id,
                               @RequestParam int creditLimit,
                               @RequestParam float interestRate,
                               Model model) {
        Credit credit = creditService.getCredit(id);
        creditService.updateCredit(credit, interestRate, creditLimit);
        return "redirect:/credits";
    }

    @PostMapping("/credits/remove/{id}")
    @PreAuthorize("hasAuthority('SUPERUSER')")
    public String deleteCredit(@PathVariable(value = "id") UUID id, Model model) {
        creditService.deleteCredit(id);
        return "redirect:/credits";
    }
}
