package io.hrushik09.tasker.boards;

public class BoardDoesNotExistException extends RuntimeException {
    public BoardDoesNotExistException(Integer id) {
        super("Board with id=" + id + " does not exist");
    }
}
