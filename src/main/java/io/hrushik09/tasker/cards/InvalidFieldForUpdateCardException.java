package io.hrushik09.tasker.cards;

public class InvalidFieldForUpdateCardException extends RuntimeException {
    public InvalidFieldForUpdateCardException(String name) {
        super("Field " + name + " not found in Card");
    }
}
