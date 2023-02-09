package com.aptech.diplom.services;

import com.aptech.diplom.TesttaskApplication;
import com.aptech.diplom.models.Bank;
import com.aptech.diplom.models.Credit;
import com.aptech.diplom.models.CreditOffer;
import com.aptech.diplom.models.CreditPayment;
import com.aptech.diplom.models.Customer;
import com.aptech.diplom.repository.CreditOfferRepository;
import com.aptech.diplom.service.CreditOfferService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TesttaskApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class CreditOfferServiceTest {

    @InjectMocks
    private CreditOfferService creditOfferService;

    @Mock
    private CreditOfferRepository creditOffersRepository;

    @Captor
    private ArgumentCaptor<CreditOffer> creditOfferCaptor;

    @Test
    public void applyCreditOfferTest() {
        Customer customer = Customer.builder()
                .name("John")
                .build();

        Credit credit = new Credit();
        credit.setCreditLimit(670000);
        credit.setInterestRate(9);

        Bank bank = new Bank();
        bank.setName("Bank");
        String startDate = "2022-07-07";
        float creditAmount = 670000;
        int numberOfMonths = 3;
        double delta = 0.01;

        when(creditOffersRepository.save(creditOfferCaptor.capture())).thenReturn(any());

        creditOfferService.applyCreditOffer(customer, credit, bank, creditAmount, credit.getInterestRate(), startDate, numberOfMonths);
        CreditOffer creditOffer = creditOfferCaptor.getValue();
        List<CreditPayment> paymentSchedule = creditOffer.getPaymentSchedule();

        assertEquals(670000, creditOffer.getCredit().getCreditLimit());
        assertEquals("Bank", creditOffer.getBank().getName());
        assertEquals("John", creditOffer.getCustomer().getName());
        assertEquals(9, creditOffer.getCredit().getInterestRate());
        assertEquals(3, paymentSchedule.size());

        assertEquals(226690.11, paymentSchedule.get(0).getPaymentOfMonth(), delta);
        assertEquals(226690.11, paymentSchedule.get(1).getPaymentOfMonth(), delta);
        assertEquals(226690.11, paymentSchedule.get(2).getPaymentOfMonth(), delta);

        assertEquals(5025.0, paymentSchedule.get(0).getPaymentOfPercentInMonth(), delta);
        assertEquals(3362.51, paymentSchedule.get(1).getPaymentOfPercentInMonth(), delta);
        assertEquals(1687.55, paymentSchedule.get(2).getPaymentOfPercentInMonth(), delta);

        assertEquals(221665.11, paymentSchedule.get(0).getPaymentOfLoanBodyInMonth(), delta);
        assertEquals(223327.6, paymentSchedule.get(1).getPaymentOfLoanBodyInMonth(), delta);
        assertEquals(225002.55, paymentSchedule.get(2).getPaymentOfLoanBodyInMonth(), delta);
    }
}
