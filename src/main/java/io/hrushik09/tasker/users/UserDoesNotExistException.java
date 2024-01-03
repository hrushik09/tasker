package io.hrushik09.tasker.users;

import io.hrushik09.tasker.advice.DoesNotExistException;

public class UserDoesNotExistException extends DoesNotExistException {
    public UserDoesNotExistException(Integer id) {
        super("User with id=" + id + " does not exist");
    }
}
