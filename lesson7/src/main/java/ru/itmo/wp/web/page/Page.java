package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Page {
    protected final UserService userService = new UserService();
    protected final ArticleService articleService = new ArticleService();

    protected void action(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    protected User getUser(HttpServletRequest request) {
        return userService.find(((User) request.getSession().getAttribute("user")).getId());
    }
}
