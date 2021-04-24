package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TalksPage extends Page {
    @Override
    protected void action(HttpServletRequest request, Map<String, Object> view) {
        if (request.getSession().getAttribute("user") == null) {
            setMessage("You need to be authorized to talk");
            throw new RedirectException("/enter");
        }
        view.put("talks", userService.findAllTalks());
        view.put("users", userService.findAllUsers());
    }

    private void addMessage(HttpServletRequest request, Map<String, Object> view) {
        Talk talk = new Talk();
        talk.setText(request.getParameter("message"));
        talk.setSourceUserId(getUser().getId());
        User targetUser = userService.findByLogin(request.getParameter("targetUser"));
        talk.setTargetUserId(targetUser.getId());
        userService.saveTalk(talk);
        throw new RedirectException("/index");
    }
}
