package com.haulmont.testtask.controllers.admin;

import com.haulmont.testtask.models.User;
import com.haulmont.testtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminPageController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    //@PreAuthorize("hasAuthority('SUPERUSER')")
    public String adminPage(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        model.addAttribute("title", "Пользователи");
        return "admin/admin";
    }

    @PostMapping("/admin/{id}/delete")
    //@PreAuthorize("hasAuthority('SUPERUSER')")
    public String deleteUser(@PathVariable(value = "id") Long id, Model model) {
        User user = userService.getUser(id);
        userService.deleteUser(user);
        return "redirect:/admin";
    }
}
