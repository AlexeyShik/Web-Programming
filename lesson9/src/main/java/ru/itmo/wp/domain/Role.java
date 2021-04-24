package ru.itmo.wp.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Role {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private Name name;

    /** @noinspection unused*/
    public Role() {
    }

    public Role(@NotNull Name name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public enum Name {
        WRITER,
        ADMIN
    }
}
