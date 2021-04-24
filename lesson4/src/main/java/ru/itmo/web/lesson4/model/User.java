package ru.itmo.web.lesson4.model;

public class User {
    public enum Color {
        RED, GREEN, BLUE
    }

    private final long id;
    private final String handle;
    private final String name;
    private final Color color;
    private final long postsCount;

    public User(long id, String handle, String name, Color color, long postsCount) {
        this.id = id;
        this.handle = handle;
        this.name = name;
        this.color = color;
        this.postsCount = postsCount;
    }

    public long getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public String getPostsCount() {
        return Long.toString(postsCount);
    }
}
