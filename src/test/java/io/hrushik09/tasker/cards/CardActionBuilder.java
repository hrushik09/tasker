package io.hrushik09.tasker.cards;

import java.time.Instant;

public class CardActionBuilder {
    private Integer id = 1;
    private String type = "card";
    private Integer cardId = 1;
    private String text = "Not important";
    private Instant due = Instant.MIN;
    private Instant createdAt = Instant.parse("2023-04-04T02:02:02Z");
    private Instant updatedAt = Instant.parse("2023-04-04T03:02:02Z");

    private CardActionBuilder() {
    }

    private CardActionBuilder(CardActionBuilder copy) {
        this.id = copy.id;
        this.type = copy.type;
        this.cardId = copy.cardId;
        this.text = copy.text;
        this.due = copy.due;
        this.createdAt = copy.createdAt;
        this.updatedAt = copy.updatedAt;
    }

    public static CardActionBuilder aCardActionBuilder() {
        return new CardActionBuilder();
    }

    public CardActionBuilder but() {
        return new CardActionBuilder(this);
    }

    public CardActionBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public CardActionBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public CardActionBuilder withCardId(Integer cardId) {
        this.cardId = cardId;
        return this;
    }

    public CardActionBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public CardActionBuilder withDue(Instant due) {
        this.due = due;
        return this;
    }

    public CardAction build() {
        CardAction cardAction = new CardAction();
        cardAction.setId(id);
        cardAction.setType(type);
        cardAction.setCardId(cardId);
        cardAction.setText(text);
        cardAction.setDue(due);
        cardAction.setCreatedAt(createdAt);
        cardAction.setUpdatedAt(updatedAt);
        return cardAction;
    }
}
