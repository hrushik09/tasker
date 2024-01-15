package io.hrushik09.tasker.cards;

import java.time.Instant;

public class DateActionDTOBuilder {
    private String type = "date";
    private Instant date = Instant.parse("2022-05-21T23:54:56Z");

    private DateActionDTOBuilder() {
    }

    private DateActionDTOBuilder(DateActionDTOBuilder copy) {
        this.type = copy.type;
        this.date = copy.date;
    }

    public static DateActionDTOBuilder aDateActionDTOBuilder() {
        return new DateActionDTOBuilder();
    }

    public DateActionDTOBuilder but() {
        return new DateActionDTOBuilder(this);
    }

    public DateActionDTOBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public DateActionDTOBuilder withDate(Instant date) {
        this.date = date;
        return this;
    }

    public DateActionDTO build() {
        return new DateActionDTO(type, date);
    }
}
