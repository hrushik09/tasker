package io.hrushik09.tasker.cards;

public class NotAllowedFieldForUpdateCardException extends RuntimeException {
    public NotAllowedFieldForUpdateCardException(String name) {
        super("Field " + name + " is not allowed for update");
    }
}
