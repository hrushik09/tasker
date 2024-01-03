package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.advice.DoesNotExistException;

public class BoardDoesNotExistException extends DoesNotExistException {
    public BoardDoesNotExistException(Integer id) {
        super("Board with id=" + id + " does not exist");
    }
}
