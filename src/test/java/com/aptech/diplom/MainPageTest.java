package com.aptech.diplom;

import com.aptech.diplom.controllers.BankController;
import com.aptech.diplom.models.UserRole;
import com.aptech.diplom.models.UserRoles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainPageTest {

    private UserRole userRole;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BankController bankController;

    @Test
    public void should_Content_BanksHeader_In_Main_Page() throws Exception {

        userRole = new UserRole();
        userRole.setRole(UserRoles.SUPERUSER);
        MockHttpServletRequestBuilder request = get("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(user("admin").password("admin").authorities(userRole))
                .with(csrf());
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Банки")));
    }
}
