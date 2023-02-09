package com.aptech.diplom.services;

import com.aptech.diplom.TesttaskApplication;
import com.aptech.diplom.models.Customer;
import com.aptech.diplom.repository.CustomerRepository;
import com.aptech.diplom.service.CustomerService;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TesttaskApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void updateCustomerTest() {
        Customer customer = Customer.builder()
                .name("John")
                .surname("Smith")
                .patronymic("O'Nil")
                .email("oldmail@mail.com")
                .phoneNumber("88005557575")
                .passportNumber("5465454565")
                .build();

        when(customerRepository.save(any())).thenReturn(customer);
        customerService.updateCustomer(customer, "Tom", "Hanks", "", "88468684848", "newemail@mail.com", "54555455");

        assertEquals(customer.getName(), "Tom");
        assertEquals(customer.getSurname(), "Hanks");
        assertEquals(customer.getPatronymic(), "");
        assertEquals(customer.getEmail(), "newemail@mail.com");
        assertEquals(customer.getPassportNumber(), "54555455");
        assertEquals(customer.getPhoneNumber(), "88468684848");
    }
}
