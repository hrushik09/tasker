package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.lists.ListBuilder;

import java.time.Instant;

import static io.hrushik09.tasker.lists.ListBuilder.aList;

public class CardBuilder {
    private Integer id = 1;
    private String title = "Not important";
    private String description = "Not important";
    private Instant start = Instant.parse("2023-01-05T12:12:12Z");
    private Instant due = Instant.parse("2023-01-10T12:12:12Z");
    private ListBuilder listBuilder = aList();
    private Instant createdAt = Instant.parse("2023-01-01T12:12:12Z");
    private Instant updatedAt = Instant.parse("2023-01-01T12:12:12Z");

    private CardBuilder() {
    }

    private CardBuilder(CardBuilder copy) {
        id = copy.id;
        title = copy.title;
        description = copy.description;
        start = copy.start;
        due = copy.due;
        listBuilder = copy.listBuilder;
        createdAt = copy.createdAt;
        updatedAt = copy.updatedAt;
    }

    public static CardBuilder aCard() {
        return new CardBuilder();
    }

    public CardBuilder but() {
        return new CardBuilder(this);
    }

    public CardBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public CardBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CardBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CardBuilder withStart(Instant start) {
        this.start = start;
        return this;
    }

    public CardBuilder withDue(Instant due) {
        this.due = due;
        return this;
    }

    public CardBuilder with(ListBuilder listBuilder) {
        this.listBuilder = listBuilder;
        return this;
    }

    public Card build() {
        Card card = new Card();
        card.setId(id);
        card.setTitle(title);
        card.setDescription(description);
        card.setStart(start);
        card.setDue(due);
        card.setList(listBuilder.build());
        card.setCreatedAt(createdAt);
        card.setUpdatedAt(updatedAt);
        return card;
    }
}
