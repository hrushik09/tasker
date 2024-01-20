package io.hrushik09.tasker.cards;

import java.time.Instant;

public class DateAction {
    private String type;
    private Instant dueAt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getDueAt() {
        return dueAt;
    }

    public void setDueAt(Instant dueAt) {
        this.dueAt = dueAt;
    }
}
