package com.haulmont.testtask.controller;

import com.haulmont.testtask.controllers.BankController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BankController bankController;

    @Test
    public void default_BankList_Should_Content_10_Rows() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(xpath("//*[@id='bank_list']/tbody/tr").nodeCount(10));
    }

    @Test
    public void should_Add_Bank_To_List_When_Adding_Bank() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/banks/add")
                .param("nameOfBank", "SuperBank");
        this.mockMvc.perform(multipart);
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(xpath("//*[@id='bank_list']/tbody/tr").nodeCount(11))
                .andExpect(content().string(containsString("SuperBank")));
    }
}
