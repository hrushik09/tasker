package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.advice.DoesNotExistException;

public class ListDoesNotExistException extends DoesNotExistException {
    public ListDoesNotExistException(int id) {
        super("List with id=" + id + " does not exist");
    }
}
