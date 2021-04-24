package ru.itmo.wp.model.domain;

public class Event extends Entity {
    private long userId;
    private TYPE type;

    public Event() { }

    public Event(long userId, TYPE type) {
        this.userId = userId;
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public enum TYPE {
        ENTER, LOGOUT
    }
}
