package io.hrushik09.tasker.cards;

import java.time.Instant;

public class DateActionBuilder {
    private Integer id;
    private String type = "date";
    private Instant dueAt = Instant.parse("2021-05-21T03:23:56Z");
    private Instant createdAt = Instant.parse("2023-01-04T02:12:02Z");
    private Instant updatedAt = Instant.parse("2023-07-04T03:02:02Z");

    private DateActionBuilder() {
    }

    private DateActionBuilder(DateActionBuilder copy) {
        this.id = copy.id;
        this.type = copy.type;
        this.dueAt = copy.dueAt;
        this.createdAt = copy.createdAt;
        this.updatedAt = copy.updatedAt;
    }

    public static DateActionBuilder aDateActionBuilder() {
        return new DateActionBuilder();
    }

    public DateActionBuilder but() {
        return new DateActionBuilder(this);
    }

    public DateActionBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public DateActionBuilder withDueAt(Instant dueAt) {
        this.dueAt = dueAt;
        return this;
    }

    public DateAction build() {
        DateAction dateAction = new DateAction();
        dateAction.setId(id);
        dateAction.setType(type);
        dateAction.setDueAt(dueAt);
        dateAction.setCreatedAt(createdAt);
        dateAction.setUpdatedAt(updatedAt);

        return dateAction;
    }
}
