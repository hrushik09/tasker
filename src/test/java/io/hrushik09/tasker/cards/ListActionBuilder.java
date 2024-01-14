package io.hrushik09.tasker.cards;

import java.time.Instant;

public class ListActionBuilder {
    private Integer id = 1;
    private String type = "list";
    private Integer listId = 1;
    private String text = "Not important";
    private Instant createdAt = Instant.parse("2023-05-04T02:02:02Z");
    private Instant updatedAt = Instant.parse("2023-05-04T03:02:02Z");

    private ListActionBuilder() {
    }

    private ListActionBuilder(ListActionBuilder copy) {
        this.id = copy.id;
        this.type = copy.type;
        this.listId = copy.listId;
        this.text = copy.text;
        this.createdAt = copy.createdAt;
        this.updatedAt = copy.updatedAt;
    }

    public static ListActionBuilder aListActionBuilder() {
        return new ListActionBuilder();
    }

    public ListActionBuilder but() {
        return new ListActionBuilder(this);
    }

    public ListActionBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ListActionBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public ListActionBuilder withListId(Integer listId) {
        this.listId = listId;
        return this;
    }

    public ListActionBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public ListAction build() {
        ListAction listAction = new ListAction();
        listAction.setId(id);
        listAction.setType(type);
        listAction.setListId(listId);
        listAction.setText(text);
        listAction.setCreatedAt(createdAt);
        listAction.setUpdatedAt(updatedAt);
        return listAction;
    }
}
