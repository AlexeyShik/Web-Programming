package ru.itmo.wp.form;

import ru.itmo.wp.domain.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PostCredentials {
    @NotEmpty
    @Size(min = 1, max = 100)
    private String title;

    @NotEmpty
    @Size(min = 1, max = 10000)
    private String text;

    private User user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
