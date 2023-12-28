package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.users.UserBuilder;

import java.time.Instant;

import static io.hrushik09.tasker.users.UserBuilder.aUser;

public class ListBuilder {
    private int id = 1;
    private String title = "Not important";
    private UserBuilder userBuilder = aUser();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private ListBuilder() {
    }

    private ListBuilder(ListBuilder copy) {
        this.id = copy.id;
        this.title = copy.title;
        this.userBuilder = copy.userBuilder;
        this.createdAt = copy.createdAt;
        this.updatedAt = copy.updatedAt;
    }

    public static ListBuilder aList() {
        return new ListBuilder();
    }

    public ListBuilder but() {
        return new ListBuilder(this);
    }

    public ListBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ListBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ListBuilder with(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
        return this;
    }

    public List build() {
        List list = new List();
        list.setId(id);
        list.setTitle(title);
        list.setUser(userBuilder.build());
        list.setCreatedAt(createdAt);
        list.setUpdatedAt(updatedAt);
        return list;
    }
}
