package com.aptech.diplom.controller;

import com.aptech.diplom.controllers.BankController;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Autowired
    private BankController bankController;

    @Test
    public void default_BankList_Should_Content_10_Rows() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(xpath("//*[@id='bank_list']/tbody/tr").nodeCount(10));
    }

    @Test
    @WithMockUser("spring")
    public void should_Add_Bank_To_List_When_Adding_Bank() throws Exception {
//        MockHttpServletRequestBuilder multipart = multipart("/banks/add")
//                .param("nameOfBank", "SuperBank");
        MockHttpServletRequestBuilder request = post("/banks/add")
                .param("nameOfBank", "SuperBank")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(user("admin").password("admin").roles("SUPERUSER"))
                .with(csrf());
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
//        this.mockMvc.perform(get("/"))
//                .andDo(print())
//                .andExpect(xpath("//*[@id='bank_list']/tbody/tr").nodeCount(11))
//                .andExpect(content().string(containsString("SuperBank")));
    }
}
