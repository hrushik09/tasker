package io.hrushik09.tasker.lists;

public class ListDoesNotExistException extends RuntimeException {
    public ListDoesNotExistException(int id) {
        super("List with id=" + id + " does not exist");
    }
}
