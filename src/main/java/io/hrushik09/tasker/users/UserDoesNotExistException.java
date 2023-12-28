package io.hrushik09.tasker.users;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(int id) {
        super("User with id=" + id + " does not exist");
    }
}
