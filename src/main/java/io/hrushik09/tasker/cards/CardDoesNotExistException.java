package io.hrushik09.tasker.cards;

public class CardDoesNotExistException extends RuntimeException {
    public CardDoesNotExistException(Integer id) {
        super("Card with id=" + id + " does not exist");
    }
}
