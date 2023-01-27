package com.haulmont.testtask.services;

import com.haulmont.testtask.TesttaskApplication;
import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.repository.BankRepository;
import com.haulmont.testtask.service.BankService;
import com.haulmont.testtask.service.CreditService;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TesttaskApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class BankServiceTest {

    @InjectMocks
    private BankService testBankService;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private CreditService creditService;

    @Test
    public void updateBankTest() {
        String[] credits = new String[] {"1f0d9e4c-9e15-11ed-a8fc-0242ac120002", "244c27c0-9e15-11ed-a8fc-0242ac120002"};
        Bank oldBank = new Bank();
        oldBank.setNameOfBank("testBank");
        Credit c1 = new Credit(5000, 1);
        Credit c2 = new Credit(5000, 1);
        c1.setId(UUID.fromString("87f72b1c-9e15-11ed-a8fc-0242ac120002"));
        c2.setId(UUID.fromString("bafe9194-9e15-11ed-a8fc-0242ac120002"));
        c1.addBank(oldBank);
        List<Credit> creditList = new ArrayList<Credit>();
        creditList.add(c1);
        oldBank.setCredits(creditList);

        CreditService mock = mock(CreditService.class);
        Credit creditMock = mock(Credit.class);
        mock(BankRepository.class);
        when(creditService.getCredit(c1.getId())).thenReturn(c1);
        when(creditService.getCredit(c2.getId())).thenReturn(c2);
        when(bankRepository.save(oldBank)).thenReturn(oldBank);

        Bank newBank = testBankService.updateBank(oldBank, credits, "newNameOfBank");

        Bank testBank = new Bank();
        testBank.setNameOfBank("newNameOfBank");
        testBank.addCredit(c1);
        testBank.addCredit(c2);
        Assertions.assertEquals(newBank.getNameOfBank(), testBank.getNameOfBank());
        Assertions.assertEquals(newBank.getCredits().size(), testBank.getCredits().size());
    }
}
