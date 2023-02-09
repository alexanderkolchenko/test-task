package com.aptech.diplom.repository;


import com.aptech.diplom.models.Bank;
import com.aptech.diplom.models.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CustomerRepositoryTest {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CreditRepository creditRepository;

    @Test
    public void whenRemoveCustomer_shouldNotRemoveBanks(){
        Customer customer = customerRepository.findAll().get(0);
        customerRepository.delete(customer);
        assertEquals(10, bankRepository.findAll().size());
    }

    @Test
    public void whenRemoveCustomer_shouldNotRemoveCredits(){
        Customer customer = customerRepository.findAll().get(0);
        customerRepository.delete(customer);
        assertEquals(10, creditRepository.findAll().size());
    }
}
