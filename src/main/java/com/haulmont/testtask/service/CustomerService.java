package com.haulmont.testtask.service;

import com.haulmont.testtask.exception_handler.NoSuchCustomerException;
import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.repository.CreditPaymentRepository;
import com.haulmont.testtask.repository.CustomerRepository;
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

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void removeCustomer(UUID id) {
        Customer customer = getCustomer(id);
        customer.getCreditOffers().forEach(x -> creditPaymentRepository.deleteByCreditOfferId(x.getId()));
        customerRepository.delete(customer);
    }
}
