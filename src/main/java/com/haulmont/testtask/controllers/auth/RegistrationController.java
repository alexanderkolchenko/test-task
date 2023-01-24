package com.haulmont.testtask.controllers.auth;

import com.haulmont.testtask.models.User;
import com.haulmont.testtask.models.UserRole;
import com.haulmont.testtask.models.UserRoles;
import com.haulmont.testtask.registration.RegistrationForm;
import com.haulmont.testtask.repository.auth.UserRepository;
import com.haulmont.testtask.repository.auth.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRolesRepository rolesRepository;

    public RegistrationController(
            UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        User user = form.toUser(passwordEncoder);
        UserRole role = rolesRepository.findByRole(UserRoles.USER);
        user.addRole(role);
        userRepo.save(form.toUser(passwordEncoder));
        return "redirect:/login";
    }
}