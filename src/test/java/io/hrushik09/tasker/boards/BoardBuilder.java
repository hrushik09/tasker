package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.users.UserBuilder;

import java.time.Instant;

import static io.hrushik09.tasker.users.UserBuilder.aUser;

public class BoardBuilder {
    private Integer id = 1;
    private String title = "Not important";
    private UserBuilder userBuilder = aUser();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private BoardBuilder() {
    }

    private BoardBuilder(BoardBuilder copy) {
        id = copy.id;
        title = copy.title;
        userBuilder = copy.userBuilder;
        createdAt = copy.createdAt;
        updatedAt = copy.updatedAt;
    }

    public static BoardBuilder aBoard() {
        return new BoardBuilder();
    }

    public BoardBuilder but() {
        return new BoardBuilder(this);
    }

    public BoardBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public BoardBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BoardBuilder with(UserBuilder userBuilder) {
        this.userBuilder = userBuilder;
        return this;
    }

    public Board build() {
        Board board = new Board();
        board.setId(id);
        board.setTitle(title);
        board.setUser(userBuilder.build());
        board.setCreatedAt(createdAt);
        board.setUpdatedAt(updatedAt);
        return board;
    }
}
