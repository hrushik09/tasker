package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.lists.ListBuilder;

import java.time.Instant;

import static io.hrushik09.tasker.lists.ListBuilder.aList;

public class CardBuilder {
    private Integer id = 1;
    private String title = "Not important";
    private ListBuilder listBuilder = aList();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    private CardBuilder() {
    }

    private CardBuilder(CardBuilder copy) {
        id = copy.id;
        title = copy.title;
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

    public CardBuilder with(ListBuilder listBuilder) {
        this.listBuilder = listBuilder;
        return this;
    }

    public Card build() {
        Card card = new Card();
        card.setId(id);
        card.setTitle(title);
        card.setList(listBuilder.build());
        card.setCreatedAt(createdAt);
        card.setUpdatedAt(updatedAt);
        return card;
    }
}
