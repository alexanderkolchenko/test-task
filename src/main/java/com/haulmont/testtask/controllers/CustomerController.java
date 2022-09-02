package com.haulmont.testtask.controllers;


import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;


@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String getCustomersPage(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
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
    public String addCustomerSubmit(@RequestParam String name, @RequestParam String surname,
                                    @RequestParam String patronymic, @RequestParam String phoneNumber,
                                    @RequestParam String email, @RequestParam String passportNumber) {
        //todo change modelattribute
        Customer customer = new Customer(surname, name, patronymic, phoneNumber, email, passportNumber);
        customerService.addCustomer(customer);
        return "redirect:/customers";
    }

    @GetMapping("/customers/{id}")
    public String editCustomer(@PathVariable(value = "id") UUID id, Model model) {
        model.addAttribute("customer", customerService.getCustomer(id));
        model.addAttribute("title", "Редактирование клиента");
        return "customers/customers_edit";
    }

    @PostMapping("/customers/{id}")
    public String updateCustomer(@PathVariable(value = "id") UUID id, @RequestParam String name, @RequestParam String surname,
                                 @RequestParam String patronymic, @RequestParam String phoneNumber,
                                 @RequestParam String email, @RequestParam String passportNumber) {
        //todo modelattribute and save with add mapping
        Customer customer = customerService.getCustomer(id);
        customerService.updateCustomer(customer, name, surname, patronymic, phoneNumber, email, passportNumber);
        return "redirect:/customers";
    }

    @PostMapping("/customers/{id}/remove")
    public String deleteCustomer(@PathVariable(value = "id") UUID id) {
        customerService.deleteCustomer(id);
        return "redirect:/customers";
    }
}
