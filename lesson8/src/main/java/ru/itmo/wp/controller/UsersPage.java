package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.User;

import javax.servlet.http.HttpSession;

@Controller
public class UsersPage extends Page {

    @GetMapping("/users/all")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "UsersPage";
    }

    @PostMapping("/users/enable/{id}")
    public String setEnabled(@PathVariable(value = "id") String id,
                             HttpSession httpSession) {
        User user = idToUser(id);
        if (user == null) {
            putMessage(httpSession, "No such user");
        } else {
            userService.setDisabled(user, false);
            putMessage(httpSession, "User with login " + user.getLogin() + " is enabled");
        }
        return "redirect:/users/all";
    }

    @PostMapping("/users/disable/{id}")
    public String setDisabled(@PathVariable(value = "id") String id,
                              HttpSession httpSession) {
        User user = idToUser(id);
        if (user == null) {
            putMessage(httpSession, "No such user");
        } else {
            userService.setDisabled(user, true);
            putMessage(httpSession, "User with login " + user.getLogin() + " is disabled");
        }
        return "redirect:/users/all";
    }
}
