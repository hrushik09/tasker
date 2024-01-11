package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.advice.DoesNotExistException;

public class ListNotInGivenBoardException extends DoesNotExistException {
    public ListNotInGivenBoardException(Integer id) {
        super("List with id=" + id + " does not exist in current board");
    }
}
