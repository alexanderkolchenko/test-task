package com.aptech.diplom.repository;

import com.aptech.diplom.models.Bank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankRepositoryTest {

    @Autowired
    private BankRepository bankRepository;

    @Test
    public void should_Add_Bank() throws Exception {
        Bank bank = bankRepository.save(new Bank("SPB"));
        assertNotNull(bank);
        assertEquals(UUID.fromString(String.valueOf(bank.getId())), bank.getId());
    }
}
