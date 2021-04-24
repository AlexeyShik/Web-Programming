package ru.itmo.wp.form.validator;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.PostCredentials;
import ru.itmo.wp.service.UserService;

@Component
public class PostCredentialsValidator implements Validator {
    private final UserService userService;

    public PostCredentialsValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return PostCredentials.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, Errors errors) {
        if (!errors.hasErrors()) {
            PostCredentials post = (PostCredentials) target;
            if (post.getUser() == null || userService.findById(post.getUser().getId()) == null) {
                errors.rejectValue("user", "user.invalid-title-or-text-or-user", "Invalid user");
            }
        }
    }
}
