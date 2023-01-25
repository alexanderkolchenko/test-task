package com.haulmont.testtask;

import com.haulmont.testtask.controllers.BankController;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
//todo @WithUserDetails
public class MainPageTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BankController bankController;

    @Test
    public void should_Content_BanksHeader_In_Main_Page() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Банки")));
    }

    //todo
    @Test
    @org.junit.Ignore
    public void content_username(){}

    //todo
    @Test
    @Ignore
    public void redirect(){}

    //todo
    @Test
    @Ignore
    public void login_correct(){}

    //todo
    @Test
    @Ignore
    public void login_incorrect(){}
}
