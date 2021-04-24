package ru.itmo.wp.form.validator;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.UserRegisterForm;
import ru.itmo.wp.service.UserService;

@Component
public class UserRegisterFormValidator implements Validator {
    private final UserService userService;

    public UserRegisterFormValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserRegisterForm.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, Errors errors) {
        if (!errors.hasErrors()) {
            UserRegisterForm registerFrom = (UserRegisterForm) target;
            if (userService.findByLogin(registerFrom.getLogin()) != null) {
                errors.rejectValue("login", "login.invalid-login-or-password", "login already in use");
            }
        }
    }
}
