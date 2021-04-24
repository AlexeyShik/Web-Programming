package ru.itmo.wp.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserCredentials {
    @NotEmpty
    @Pattern(regexp = "[a-zA-Z]{2,24}", message = "expected from 2 to 24 latin letters")
    private String login;

    @NotEmpty
    @Size(min = 1, max = 60)
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
