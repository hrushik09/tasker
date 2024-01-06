package io.hrushik09.tasker.cards;

public class InvalidFieldForUpdateCardException extends RuntimeException {
    public InvalidFieldForUpdateCardException(String key) {
        super("Field " + key + " not found in Card");
    }
}
