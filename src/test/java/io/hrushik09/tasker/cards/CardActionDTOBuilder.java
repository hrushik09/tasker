package io.hrushik09.tasker.cards;

public class CardActionDTOBuilder {
    private String type = "card";
    private Integer id = 1;
    private String text = "Not important";

    private CardActionDTOBuilder() {
    }

    private CardActionDTOBuilder(CardActionDTOBuilder copy) {
        this.type = copy.type;
        this.id = copy.id;
        this.text = copy.text;
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

    public CardActionDTO build() {
        return new CardActionDTO(type, id, text);
    }
}
