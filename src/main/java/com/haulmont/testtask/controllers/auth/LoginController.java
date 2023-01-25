package com.haulmont.testtask.controllers.auth;

import com.haulmont.testtask.repository.auth.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

//todo delete it
@Controller
@RequestMapping("/logi")
public class LoginController {

    private UserRepository userRepository;

    @GetMapping
    public String loginForm() {
        return "login";
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String processLogin(@RequestBody MultiValueMap<String, String> formData) {
        String password = formData.get("password").get(0);
        return "redirect:/";
    }
}
