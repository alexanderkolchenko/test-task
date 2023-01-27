package com.aptech.diplom.service;

import com.aptech.diplom.repository.CreditPaymentRepository;
import com.aptech.diplom.repository.CustomerRepository;
import com.aptech.diplom.exception_handler.exception.NoSuchCustomerException;
import com.aptech.diplom.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditPaymentRepository creditPaymentRepository;

    public Customer getCustomer(UUID id) {
        return customerRepository.findById(id).orElseThrow(() -> new NoSuchCustomerException("There is no customer with id " + id));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Customer customer, String name, String surname, String patronymic, String phoneNumber, String email, String passportNumber) {
        customer.setName(name);
        customer.setSurname(surname);
        customer.setPatronymic(patronymic);
        customer.setPhoneNumber(phoneNumber);
        customer.setEmail(email);
        customer.setPassportNumber(passportNumber);
        return customerRepository.save(customer);
    }

    public Customer deleteCustomer(UUID id) {
        Customer customer = getCustomer(id);
        customer.getCreditOffers().forEach(x -> creditPaymentRepository.deleteByCreditOfferId(x.getId()));
        customerRepository.delete(customer);
        return customer;
    }
}
