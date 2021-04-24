package ru.itmo.wp.controller;

import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.exception.ValidationException;
import ru.itmo.wp.form.UserRegisterForm;
import ru.itmo.wp.form.validator.UserRegisterFormValidator;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/api/1")
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;
    private final UserRegisterFormValidator userRegisterFormValidator;

    public UserController(JwtService jwtService, UserService userService, UserRegisterFormValidator userRegisterFormValidator) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRegisterFormValidator = userRegisterFormValidator;
    }

    @InitBinder("userRegisterForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userRegisterFormValidator);
    }

    @GetMapping("/users/auth")
    public User findUserByJwt(@RequestParam String jwt) {
        return jwtService.find(jwt);
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User findById(@PathVariable String id) {
        try {
            return userService.findById(Long.parseLong(id));
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/users/find")
    public User findByLogin(@NonNull @NotEmpty String login) {
        return userService.findByLogin(login);
    }

    @PostMapping("/users")
    public void register(@RequestBody @Valid UserRegisterForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        userService.save(form.getLogin(), form.getName(), form.getPassword());
    }
}
