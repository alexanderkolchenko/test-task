package com.aptech.diplom.controller;

import com.aptech.diplom.controllers.BankController;
import com.aptech.diplom.models.User;
import com.aptech.diplom.models.UserRole;
import com.aptech.diplom.models.UserRoles;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.security.auth.Subject;
import java.security.Principal;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource("/application-test.properties")
@Sql(value = {"/schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
public class BankControllerTest {

    private UserRole userRole;
    private User user;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        userRole = new UserRole();
        userRole.setRole(UserRoles.SUPERUSER);
        user = new User();
    }

    @Autowired
    private BankController bankController;

    @Test
    public void defaultBankList_ShouldContent10Rows() throws Exception {
        this.mockMvc.perform(get("/").with(user("admin").authorities(userRole)))
                .andDo(print())
                .andExpect(xpath("//*[@id='bank_list']/tbody/tr").nodeCount(10));
    }

    @Test
    @WithMockUser("spring")
    public void shouldAddBankToList_AfterAddingBank() throws Exception {
        MockHttpServletRequestBuilder request = post("/banks/add")
                .param("nameOfBank", "SuperBank")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(user("admin").password("admin").authorities(userRole))
                .with(csrf());
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().is3xxRedirection());
        this.mockMvc.perform(get("/").with(user("admin").authorities(userRole)))
                .andDo(print())
                .andExpect(xpath("//*[@id='bank_list']/tbody/tr").nodeCount(11))
                .andExpect(content().string(containsString("SuperBank")));
    }

}
