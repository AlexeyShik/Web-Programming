package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class UsersPage extends Page {
    private void findAll(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", userService.findAll());
    }

    private void findUser(HttpServletRequest request, Map<String, Object> view) {
        view.put("user", userService.find(Long.parseLong(request.getParameter("userId"))));
    }

    private void setAdmin(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        validateSessionUser(request);
        User user = validateUserId(request, view);
        userService.setAdmin(user);
        view.put("admin", true);
    }

    private void unsetAdmin(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        validateSessionUser(request);
        User user = validateUserId(request, view);
        userService.unsetAdmin(user);
        view.put("admin", false);
    }

    private User validateUserId(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        long id;
        User user;
        try {
            id = Long.parseLong(request.getParameter("id"));
            user = userService.find(id);
            view.put("id", id);
        } catch (NumberFormatException e) {
            throw new ValidationException("Unknown userId");
        }
        return user;
    }

    private void validateSessionUser(HttpServletRequest request) throws ValidationException {
        User user = getUser(request);
        if (user == null) {
            throw new ValidationException("You must be logged");
        }
        user = userService.find(user.getId());
        if (user == null || !user.isAdmin()) {
            throw new ValidationException("You must be admin");
        }
    }
}
