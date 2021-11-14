package com.haulmont.testtask.controllers;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.CreditOffer;
import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.repository.BankRepository;
import com.haulmont.testtask.repository.CreditOfferRepository;
import com.haulmont.testtask.repository.CreditPaymentRepository;
import com.haulmont.testtask.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CreditOfferRepository creditOfferRepository;

    @Autowired
    BankRepository bankRepository;

    @Autowired
    CreditPaymentRepository creditPaymentRepository;

    @GetMapping("/customers")
    public String getCustomer(Model model) {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll().forEach(customers::add);
        model.addAttribute("customers", customers);
        model.addAttribute("title", "Клиенты");
        model.addAttribute("numberOfClientInTable", 1);
        return "customers/customers";
    }

    @GetMapping("/customers/add")
    public String addCustomerPage(Model model) {
        model.addAttribute("title", "Добавить клиента");
        return "customers/customers_add";
    }

    @PostMapping("/customers/add")
    //todo valid
    public String addCustomerSubmit(@RequestParam String name, @RequestParam String surname,
                                    @RequestParam String patronymic, @RequestParam String phoneNumber,
                                    @RequestParam String email, @RequestParam String passportNumber, Model model) {
        Customer customer = new Customer(surname, name, patronymic, phoneNumber, email, passportNumber);
        customerRepository.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/customers/{id}")
    public String editCustomer(@PathVariable(value = "id") UUID id, Model model) {
        System.out.println(id);
        if (!customerRepository.existsById(id)) {
            return "redirect:/";
        }
        Customer customer = customerRepository.findById(id).orElse(new Customer());
        model.addAttribute("customer", customer);
        model.addAttribute("title", "Редактирование клиента");
        return "customers/customers_edit";
    }

    @PostMapping("/customers/{id}")
    public String updateCustomer(@PathVariable(value = "id") UUID id, @RequestParam String name, @RequestParam String surname,
                                 @RequestParam String patronymic, @RequestParam String phoneNumber,
                                 @RequestParam String email, @RequestParam String passportNumber, Model model) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        customer.setName(name);
        customer.setSurname(surname);
        customer.setPatronymic(patronymic);
        customer.setPhoneNumber(phoneNumber);
        customer.setEmail(email);
        customer.setPassportNumber(passportNumber);
        customerRepository.save(customer);
        return "redirect:/customers";
    }

    @PostMapping("/customers/{id}/remove")
    public String deleteCustomer(@PathVariable(value = "id") UUID id, Model model) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        for (CreditOffer co : customer.getCreditOffers()) {
            System.out.println(co.getId());
            creditPaymentRepository.deleteCreditPaymentById(co.getId());

        }
        creditOfferRepository.deleteCreditOfferByCustomer(id);
        bankRepository.findAll().forEach((b) -> b.deleteCustomerFromBank(customer));
        customer.getCreditOffers().clear();
        customerRepository.delete(customer);
        return "redirect:/customers";
    }




}
