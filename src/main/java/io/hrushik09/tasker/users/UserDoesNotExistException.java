package io.hrushik09.tasker.users;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(Integer id) {
        super("User with id=" + id + " does not exist");
    }
}
