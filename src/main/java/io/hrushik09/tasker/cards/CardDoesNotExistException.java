package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.advice.DoesNotExistException;

public class CardDoesNotExistException extends DoesNotExistException {
    public CardDoesNotExistException(Integer id) {
        super("Card with id=" + id + " does not exist");
    }
}
