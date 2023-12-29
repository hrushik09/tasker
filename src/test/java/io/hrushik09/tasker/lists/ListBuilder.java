package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.boards.BoardBuilder;

import java.time.Instant;

import static io.hrushik09.tasker.boards.BoardBuilder.aBoard;

public class ListBuilder {
    private Integer id = 1;
    private String title = "Not important";
    private BoardBuilder boardBuilder = aBoard();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private ListBuilder() {
    }

    private ListBuilder(ListBuilder copy) {
        id = copy.id;
        title = copy.title;
        boardBuilder = copy.boardBuilder;
        createdAt = copy.createdAt;
        updatedAt = copy.updatedAt;
    }

    public static ListBuilder aList() {
        return new ListBuilder();
    }

    public ListBuilder but() {
        return new ListBuilder(this);
    }

    public ListBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ListBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ListBuilder with(BoardBuilder boardBuilder) {
        this.boardBuilder = boardBuilder;
        return this;
    }

    public List build() {
        List list = new List();
        list.setId(id);
        list.setTitle(title);
        list.setBoard(boardBuilder.build());
        list.setCreatedAt(createdAt);
        list.setUpdatedAt(updatedAt);
        return list;
    }
}
