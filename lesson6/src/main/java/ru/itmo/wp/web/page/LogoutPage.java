package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class LogoutPage extends Page {
    protected void action(HttpServletRequest request, Map<String, Object> view) {
        userService.saveEvent(new Event(getUser().getId(), Event.TYPE.LOGOUT));

        request.getSession().removeAttribute("user");
        request.getSession().setAttribute("message", "Good bye. Hope to see you soon!");

        throw new RedirectException("/index");
    }
}
