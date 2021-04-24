package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itmo.wp.domain.User;

import java.util.List;

@Controller
public class UserPage extends Page {

    @GetMapping("/user/{id}")
    public String users(Model model, @PathVariable(value = "id") String id) {
        User user = idToUser(id);
        model.addAttribute("users", user == null ? null : List.of(user));
        return "UserPage";
    }

    @GetMapping("/user/")
    public String users(Model model) {
        model.addAttribute("users", null);
        return "UserPage";
    }
}
