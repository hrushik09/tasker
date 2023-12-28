package io.hrushik09.tasker.users;

import java.time.Instant;

public class UserBuilder {
    private Integer id = 1;
    private String name = "Some name";
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private UserBuilder() {
    }

    private UserBuilder(UserBuilder copy) {
        this.id = copy.id;
        this.name = copy.name;
        this.createdAt = copy.createdAt;
        this.updatedAt = copy.updatedAt;
    }

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder but() {
        return new UserBuilder(this);
    }

    public UserBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setCreatedAt(createdAt);
        user.setUpdatedAt(updatedAt);
        return user;
    }
}
