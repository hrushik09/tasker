package io.hrushik09.tasker.cards;

public class ListActionDTOBuilder {
    private String type = "list";
    private Integer id = 1;
    private String text = "Not important";

    private ListActionDTOBuilder() {
    }

    private ListActionDTOBuilder(ListActionDTOBuilder copy) {
        this.type = copy.type;
        this.id = copy.id;
        this.text = copy.text;
    }

    public static ListActionDTOBuilder aListActionDTO() {
        return new ListActionDTOBuilder();
    }

    public ListActionDTOBuilder but() {
        return new ListActionDTOBuilder(this);
    }

    public ListActionDTOBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public ListActionDTOBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ListActionDTOBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public ListActionDTO build() {
        return new ListActionDTO(type, id, text);
    }
}
