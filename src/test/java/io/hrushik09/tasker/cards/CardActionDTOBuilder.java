package io.hrushik09.tasker.cards;

import java.time.Instant;

public class CardActionDTOBuilder {
    private String type = "card";
    private Integer id = 1;
    private String text = "Not important";
    private Instant due = Instant.parse("2024-01-01T01:43:12Z");

    private CardActionDTOBuilder() {
    }

    private CardActionDTOBuilder(CardActionDTOBuilder copy) {
        this.type = copy.type;
        this.id = copy.id;
        this.text = copy.text;
        this.due = copy.due;
    }

    public static CardActionDTOBuilder aCardActionDTO() {
        return new CardActionDTOBuilder();
    }

    public CardActionDTOBuilder but() {
        return new CardActionDTOBuilder(this);
    }

    public CardActionDTOBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public CardActionDTOBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public CardActionDTOBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public CardActionDTOBuilder withDue(Instant due) {
        this.due = due;
        return this;
    }

    public CardActionDTO build() {
        return new CardActionDTO(type, id, text, due);
    }
}
