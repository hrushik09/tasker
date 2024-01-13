package io.hrushik09.tasker.cards;

import java.time.Instant;

public class CardMaxDetailsDTOBuilder {
    private Integer id = 1;
    private String title = "Not important";
    private String description = "Not important";
    private Instant start = Instant.parse("2023-03-23T12:34:23Z");
    private Instant due = Instant.parse("2023-03-23T12:34:23Z");
    private Integer listId = 1;
    private Instant createdAt = Instant.parse("2023-03-23T12:34:23Z");
    private Instant updatedAt = Instant.parse("2023-03-23T12:34:23Z");

    private CardMaxDetailsDTOBuilder() {
    }

    private CardMaxDetailsDTOBuilder(CardMaxDetailsDTOBuilder copy) {
        id = copy.id;
        title = copy.title;
        description = copy.description;
        start = copy.start;
        due = copy.due;
        listId = copy.listId;
        createdAt = copy.createdAt;
        updatedAt = copy.updatedAt;
    }

    public static CardMaxDetailsDTOBuilder aCardMaxDetailsDTO() {
        return new CardMaxDetailsDTOBuilder();
    }

    public CardMaxDetailsDTOBuilder but() {
        return new CardMaxDetailsDTOBuilder(this);
    }

    public CardMaxDetailsDTOBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public CardMaxDetailsDTOBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public CardMaxDetailsDTOBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CardMaxDetailsDTOBuilder withStart(Instant start) {
        this.start = start;
        return this;
    }

    public CardMaxDetailsDTOBuilder withDue(Instant due) {
        this.due = due;
        return this;
    }

    public CardMaxDetailsDTOBuilder withListId(Integer listId) {
        this.listId = listId;
        return this;
    }

    public CardMaxDetailsDTOBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public CardMaxDetailsDTOBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public CardMaxDetailsDTO build() {
        return new CardMaxDetailsDTO(id, title, description, start, due, listId, createdAt, updatedAt);
    }
}
